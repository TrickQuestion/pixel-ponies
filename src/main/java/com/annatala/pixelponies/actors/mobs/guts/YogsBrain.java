package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.noosa.Camera;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Amok;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Sleep;
import com.annatala.pixelponies.actors.buffs.Terror;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.pixelponies.effects.particles.SparkParticle;
import com.annatala.pixelponies.levels.traps.LightningTrap;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class YogsBrain extends Boss {

    private static final float TIME_TO_ZAP	= 3f;
    private static final float TIME_TO_SUMMON	= 3f;

    {

        hp(ht(150));
        defenseSkill = 30;

        EXP = 25;

        RESISTANCES.add( LightningTrap.Electricity.class );
        RESISTANCES.add(ToxicGas.class);

        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Sleep.class);
        IMMUNITIES.add(Terror.class);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        //Paralysis proc
        if (Random.Int(5) < 3 && !Random.luckBonus()){
            Buff.affect(enemy, Paralysis.class);
        }
        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(10, 20);
    }

    @Override
    public int attackSkill( Char target ) {
        return 31;
    }

    @Override
    public int dr() {
        return 12;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return Ballistica.cast(getPos(), enemy.getPos(), false, true) == enemy.getPos();
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.distance( getPos(), enemy.getPos() ) <= 1) {

            return super.doAttack( enemy );

        } else {

            boolean visible = Dungeon.level.fieldOfView[getPos()] || Dungeon.level.fieldOfView[enemy.getPos()];
            if (visible) {
                getSprite().zap( enemy.getPos() );
            }

            spend( TIME_TO_ZAP );

            if (hit( this, enemy, true )) {
                int dmg = Random.Int( 20, 36 );
                if (Dungeon.level.water[enemy.getPos()] && !enemy.flying) {
                    dmg *= 2f;
                }
                enemy.damage( dmg, LightningTrap.LIGHTNING );

                enemy.getSprite().centerEmitter().burst( SparkParticle.FACTORY, 3 );
                enemy.getSprite().flash();

                if (enemy == Dungeon.hero) {
                    Camera.main.shake( 2, 0.3f );

                }
            } else {
                enemy.getSprite().showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

            return !visible;
        }
    }

	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}

    @Override
    public boolean act() {

        if (Random.Int(12) < 6 || Random.luckBonus()){
            return super.act();
        }

        int nightmarePos = Dungeon.level.getEmptyCellNextTo(getPos());

        spend( TIME_TO_SUMMON );

        if (Dungeon.level.cellValid(nightmarePos)) {
            Nightmare nightmare = new Nightmare();
            nightmare.setPos(nightmarePos);
            Dungeon.level.spawnMob(nightmare, 0);
            Actor.addDelayed(new Pushing(nightmare, getPos(), nightmare.getPos()), -1);

            Sample.INSTANCE.play(Assets.SND_CURSED);
        }

        return super.act();
    }
}
