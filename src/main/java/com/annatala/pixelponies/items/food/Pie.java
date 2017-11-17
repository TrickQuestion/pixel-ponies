package com.annatala.pixelponies.items.food;

import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.Statistics;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.buffs.Vertigo;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.effects.SpellSprite;
import com.annatala.pixelponies.items.scrolls.ScrollOfRecharging;
import com.annatala.pixelponies.items.weapon.missiles.MissileWeapon;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.utils.GLog;

import java.util.ArrayList;

/**
 * Created by Trickster on 10/21/2016.
 */

public abstract class Pie extends MissileWeapon {

    public static final float TIME_TO_EAT	= 3f;

    public static final String AC_EAT = Game.getVar(R.string.Food_ACEat);

    public float energy   = 0;
    public String message = Game.getVar(R.string.Food_Message);

    {
        stackable = true;
    }

    public boolean isFunnySplat() {
        return false;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_EAT );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals( AC_EAT )) {

            detach( hero.belongings.backpack );

            hero.buff( Hunger.class ).satisfy(energy);
            GLog.i( message );

            switch (hero.heroClass) {
                case EARTH_PONY:
                    if (hero.hp() < hero.ht()) {
                        hero.hp(Math.min( hero.hp() + 5, hero.ht() ));
                        hero.getSprite().emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                    }
                    break;
                case UNICORN:
                    hero.belongings.charge( false );
                    ScrollOfRecharging.charge( hero );
                    break;
                case PEGASUS:
                case ZEBRA:
                case NIGHTWING:
                    break;
            }

            hero.getSprite().operate( hero.getPos() );
            hero.busy();
            SpellSprite.show( hero, SpellSprite.FOOD );
            Sample.INSTANCE.play( Assets.SND_EAT );

            hero.spend( TIME_TO_EAT );

            Statistics.foodEaten++;
            Badges.validateFoodEaten();

        } else if (action.equals(AC_THROW)) {
            setCurUser(hero);
            curItem = this;
            doThrow(hero);

            // For the big picture: there are six stats, and each stat can be boosted nine times.
            // Honesty is only potions. Loyalty is scrolls, Magic is spellbooks. Kindness and Generosity are
            // from quests. Laughter is the strange one: four one-use items, and then five times doing
            // something I forgot. FunnyGlasses (wear them and they trigger defensively, are not set up yet), and SeltzerBottle
            // (which should let you put out fire three times in addition to the +1 Laughter from spraying an enemy).

        } else {

            super.execute( hero, action );

        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isFliesStraight() {
        return false;
    }

    @Override
    public boolean isFliesFastRotating() {
        return true;
    }

}
