package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.items.rings.BlackSkull;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.ToxicGas;
import com.annatala.pixelponies.actors.buffs.Amok;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Sleep;
import com.annatala.pixelponies.actors.buffs.Terror;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.pixelponies.items.potions.PotionOfHealing;
import com.annatala.pixelponies.items.wands.WandOfBlink;
import com.annatala.pixelponies.items.weapon.enchantments.Death;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.Callback;
import com.annatala.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by DeadDie on 12.02.2016
 */
public class Lich extends Boss {

    private static final int SKULLS_BY_DEFAULT	= 3;
    private static final int SKULLS_MAX	= 4;
    private static final int HEALTH	= 200;
    private int skullTimer = 5;
    private static final int JUMP_DELAY = 6;

    private RunicSkull activatedSkull;

    public HashSet<RunicSkull> skulls   = new HashSet<>();

    public Lich() {
        hp(ht(HEALTH));
        EXP = 25;
        defenseSkill = 23;

        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( ToxicGas.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Death.class );
        IMMUNITIES.add( Amok.class );
        IMMUNITIES.add( Blindness.class );
        IMMUNITIES.add( Sleep.class );
    }

    private int timeToJump = JUMP_DELAY;

    @Override
    protected boolean getCloser( int target ) {
        if (Dungeon.level.fieldOfView[target]) {
            jump();
            return true;
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return Ballistica.cast( getPos(), enemy.getPos(), false, true ) == enemy.getPos();
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        timeToJump--;
        if (timeToJump <= 0 && Dungeon.level.adjacent( getPos(), enemy.getPos() )) {
            jump();
            return true;
        } else {
            getSprite().zap(enemy.getPos());
            return super.doAttack( enemy );
        }
    }

    private void jump() {
        timeToJump = JUMP_DELAY;
        int newPos;
        do {
            newPos = Random.Int( Dungeon.level.getLength() );
        } while (
                !Dungeon.level.fieldOfView[newPos] ||
                        !Dungeon.level.passable[newPos] ||
                        Dungeon.level.adjacent( newPos, getEnemy().getPos() ) ||
                        Actor.findChar( newPos ) != null);

        getSprite().move( getPos(), newPos );
        move( newPos );
        spend( 1 / speed() );
    }

    //Runic skulls handling
    //***

    protected void activateRandomSkull(){
        if (!skulls.isEmpty()){
            if (activatedSkull != null){
                activatedSkull.Deactivate();
            }

            RunicSkull skull = getRandomSkull();
            if(skull == null){
                activatedSkull = null;
            } else{
                skull.Activate();
                activatedSkull = skull;
            }
        }
    }

    public RunicSkull getRandomSkull() {
        while(!skulls.isEmpty()){
            RunicSkull skull = Random.element(skulls);
            if(skull.isAlive()){
                return skull;
            }
            else{
                skulls.remove(skull);
            }
        }
        return null;
    }

    public void useSkull(){
        PlayZap();

        switch (activatedSkull.getKind()) {
            case RunicSkull.RED_SKULL:
                PotionOfHealing.heal(this,0.07f * skulls.size());
                break;

            case RunicSkull.BLUE_SKULL:
                List<Integer> occupiedCells = new ArrayList<>();
                int i = 0;
                while (i < skulls.size()){
                    int pos = Dungeon.level.getEmptyCellNextTo(getPos());
                    if (Dungeon.level.cellValid(pos)) {
                        if (!occupiedCells.contains(pos)) {
                            Skeleton skeleton = new Skeleton();
                            skeleton.setPos(pos);
                            Dungeon.level.spawnMob(skeleton, 0);
                            Actor.addDelayed(new Pushing(skeleton, getPos(), skeleton.getPos()), -1);
                            i++;
                        }
                    }
                }
                occupiedCells.clear();
                break;

            case RunicSkull.GREEN_SKULL:
                GameScene.add( Blob.seed( getPos(), 30 * skulls.size(), ToxicGas.class ) );
                break;
        }
    }

    //***

    @Override
    protected boolean act() {

        activateRandomSkull();
        if (activatedSkull != null) {
            useSkull();
        }
        postpone(skullTimer);
        return super.act();
    }


    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 14, 21 );
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (activatedSkull != null)
        {
            if(activatedSkull.getKind() == RunicSkull.PURPLE_SKULL){
                return 0;
            }
        }

        // I don't think this is really good or bad exactly, so no luck bonus here.
        if (Random.Int(2) == 0 && this.isAlive()){
            jump();
        }

        return damage;
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int dr() {
        return 5;
    }

    @Override
    public void die( Object cause ) {
        GameScene.bossSlain();
        Dungeon.level.drop( new BlackSkull(), getPos() ).sprite.drop();
        super.die( cause );

        //Kill everthing
        skulls.clear();
        Mob mob = Dungeon.level.getRandomMob();
        while(mob != null){
            mob.remove();
            mob = Dungeon.level.getRandomMob();
        }
        Badges.validateBossSlain(Badges.Badge.LICH_SLAIN);
    }

    public void spawnSkulls(){

        int nSkulls = SKULLS_BY_DEFAULT;
        if(Game.getDifficulty() == 0){
            nSkulls = 2;
        }
        else if(Game.getDifficulty() > 2){
            nSkulls = SKULLS_MAX;
        }

        List<Integer> occupiedPedestals = new ArrayList<>();
        int i = 0;
        while (i < nSkulls){
            int skullCell = Dungeon.level.getRandomTerrainCell(Terrain.PEDESTAL);
            if (Dungeon.level.cellValid(skullCell)) {
                if (!occupiedPedestals.contains(skullCell)) {
                    RunicSkull skull = RunicSkull.makeNewSkull(i);
                    Dungeon.level.spawnMob(skull);
                    WandOfBlink.appear(skull, skullCell);
                    occupiedPedestals.add(skullCell);
                    skulls.add(skull);
                    i++;
                }
            }
        }
        occupiedPedestals.clear();
    }

    public void PlayZap() {
        getSprite().zap(
                getPos(),
                new Callback() {
                    @Override
                    public void call() {
                    }
                });
    }
}
