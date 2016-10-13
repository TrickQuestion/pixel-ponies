/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.annatala.pixelponies.items.barding;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroSubClass;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.scenes.CellSelector;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.sprites.HeroSpriteDef;
import com.annatala.utils.GLog;
import com.annatala.utils.Callback;

public class RoyalBarding extends ClassBarding {

    private static int LEAP_TIME	= 1;
    private static int SHOCK_TIME	= 3;

    private static final String AC_SPECIAL = Game.getVar(R.string.RogueBarding_ACSpecial);

    private static final String TXT_NOT_ROYAL_GUARD	= Game.getVar(R.string.RoyalBarding_NotRoyalGuard);

    {
        image = 8;
    }

    @Override
    public String special() {
        return AC_SPECIAL;
    }

    @Override
    public void doSpecial() {
        GameScene.selectCell( leaper );
    }

    @Override
    public boolean doEquip( Hero hero ) {
        if (hero.subClass != HeroSubClass.ROYAL_GUARD) {
            return super.doEquip( hero );
        } else {
            GLog.w( TXT_NOT_ROYAL_GUARD );
            return false;
        }
    }

    @Override
    public String desc() {
        return Game.getVar(R.string.RoyalBarding_Desc);
    }

    protected static CellSelector.Listener leaper = new  CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {
            if (target != null && target != getCurUser().getPos()) {

                int cell = Ballistica.cast( getCurUser().getPos(), target, false, true );
                if (Actor.findChar( cell ) != null && cell != getCurUser().getPos()) {
                    cell = Ballistica.trace[Ballistica.distance - 2];
                }

                getCurUser().hp(getCurUser().hp() - (getCurUser().hp() / 3));

                getCurUser().checkIfFurious();

                Invisibility.dispel(getCurUser());

                final int dest = cell;
                getCurUser().busy();
                ((HeroSpriteDef)getCurUser().getSprite()).jump( getCurUser().getPos(), cell, new Callback() {
                    @Override
                    public void call() {
                        getCurUser().move( dest );
                        Dungeon.level.press( dest, getCurUser() );
                        Dungeon.observe();

                        for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
                            Char mob = Actor.findChar( getCurUser().getPos() + Level.NEIGHBOURS8[i] );
                            if (mob != null && mob != getCurUser()) {
                                Buff.prolong( mob, Paralysis.class, SHOCK_TIME );
                            }
                        }

                        CellEmitter.center( dest ).burst( Speck.factory( Speck.DUST ), 10 );
                        Camera.main.shake( 2, 0.5f );

                        getCurUser().spendAndNext( LEAP_TIME );
                    }
                } );
            }
        }

        @Override
        public String prompt() {
            return Game.getVar(R.string.RoyalBarding_Prompt);
        }
    };
}