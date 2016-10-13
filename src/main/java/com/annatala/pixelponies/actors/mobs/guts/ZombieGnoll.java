package com.annatala.pixelponies.actors.mobs.guts;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.effects.CellEmitter;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.Gold;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.utils.GLog;
import com.annatala.utils.Random;

/**
 * Created by DeadDie on 12.02.2016
 */
public class ZombieGnoll extends Mob {
    {
        hp(ht(210));
        defenseSkill = 27;

        EXP = 7;
        maxLvl = 35;

        loot = Gold.class;
        lootChance = 0.02f;

        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(ToxicGas.class);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(15, 35);
    }

    @Override
    public int attackSkill( Char target ) {
        return 25;
    }

    @Override
    public int dr() {
        return 20;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        if (Random.Int(100) < 65 && cause != Burning.class && !Random.luckBonus()){
            ressurrect();

            CellEmitter.center(this.getPos()).start(Speck.factory(Speck.BONE), 0.3f, 3);
            Sample.INSTANCE.play(Assets.SND_DEATH);
            if (Dungeon.visible[getPos()]) {
                getSprite().showStatus( CharSprite.NEGATIVE, Game.getVar(R.string.Goo_StaInfo1));
                GLog.n(Game.getVar(R.string.ZombieGnoll_Info));
            }

        }

        if (Dungeon.hero.lvl() <= maxLvl + 2) {
            dropLoot();
        }
    }
}
