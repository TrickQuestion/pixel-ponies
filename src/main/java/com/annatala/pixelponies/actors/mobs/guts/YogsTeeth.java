package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.effects.Devour;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Amok;
import com.annatala.pixelponies.actors.buffs.Bleeding;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Sleep;
import com.annatala.pixelponies.actors.buffs.Terror;
import com.annatala.pixelponies.actors.mobs.Boss;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class YogsTeeth extends Boss {
    {
        hp(ht(150));
        defenseSkill = 44;

        EXP = 26;

        RESISTANCES.add(ToxicGas.class);

        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Sleep.class);
        IMMUNITIES.add(Terror.class);
    }


    @Override
    public int attackProc( Char enemy, int damage ) {
        //Life drain proc
        if (Random.Int(5) < 3 && !Random.luckBonus()){
            CellEmitter.center(this.getPos()).start(Speck.factory(Speck.HEALING), 0.3f, 3);
            this.hp(this.hp() + damage );
        }
        //Bleeding proc
        if (Random.Int(5) < 3 && !Random.luckBonus()){
            Buff.affect(enemy, Bleeding.class).set(damage);
        }
        //Double damage proc
        if (Random.Int(5) < 3 && !Random.luckBonus()){
            Devour.hit(enemy);
            Sample.INSTANCE.play(Assets.SND_BITE);
            return damage*2;
        }
        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(40, 40);
    }

    @Override
    public int attackSkill( Char target ) { return 36; }

    @Override
    public int dr() { return 21; }
}
