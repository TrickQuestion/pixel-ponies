package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Amok;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Sleep;
import com.annatala.pixelponies.actors.buffs.Terror;
import com.annatala.pixelponies.items.weapon.enchantments.Death;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 07.07.2016
 */
public class UndeadMob extends Mob {
    {
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( ToxicGas.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Death.class );
        IMMUNITIES.add( Amok.class );
        IMMUNITIES.add( Blindness.class );
        IMMUNITIES.add( Sleep.class );
    }

    @Override
    public void add( Buff buff ) {
        if(!Dungeon.isLoading()) {
            if (buff instanceof Burning) {
                damage(Random.NormalIntRange(1, ht() / 8), buff);
            }
        }
        super.add( buff );
    }

}