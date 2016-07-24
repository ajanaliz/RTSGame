package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.projectile.Projectile;
import com.teamname.finalproject.game.gameobject.projectile.Spell;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.SpriteSheet;

import java.awt.*;
import java.util.Random;

public class Wizard extends RangedFighter {

    public Wizard(double x, double y, ObjectType type, PlayerID myOwner,
                  int MAXHEALTH, Kingdom kingdom, int healPerTick, int fightrange) {
        super(x, y, type, myOwner, MAXHEALTH, kingdom, healPerTick, fightrange);

    }

    @Override
    public void shoot(double dir) {
        Random random = new Random();
        int rand = random.nextInt(4);
        switch (rand) {
            case 0:
                Projectile p = new Spell(x + blockSize / 2, y + blockSize / 2, ObjectType.FIRE_SPELL, dir, getDirection(), this);
                Game.getLevel().getProjectiles().add(p);

                break;
            case 1:
                Projectile z = new Spell(x + blockSize / 2, y + blockSize / 2, ObjectType.WATER_SPELL, dir, getDirection(), this);
                Game.getLevel().getProjectiles().add(z);

                break;
            case 2:
                Projectile s = new Spell(x + blockSize / 2, y + blockSize / 2, ObjectType.ICE_SPELL, dir, getDirection(), this);
                Game.getLevel().getProjectiles().add(s);

                break;

            default:
                Projectile c = new Spell(x + blockSize / 2, y + blockSize / 2, ObjectType.EARTH_SPELL, dir, getDirection(), this);
                Game.getLevel().getProjectiles().add(c);

                break;
        }

//		enemy=null;
    }

    @Override
    public void render(Graphics g) {
        if (state != State.DEAD && state != State.BOARDED) {
            Level.hud.update(health, MAXHEALTH);
            Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));
        }
        switch (loopUpStates(state.getStateID())) {
            case MOVE:
            case MOVETOFERRY:
            case MOVETOFIGHT:
                if (frame >= SpriteSheet.WIZARDWALKING.getFramesPerDir())
                    frame = 0;
                g.drawImage(SpriteSheet.WIZARDWALKING.getSprites().get(SpriteSheet.WIZARDWALKING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.WIZARDWALKING.getFramesPerDir();
                break;
            case FIGHTING:
                if (frame >= SpriteSheet.WIZARDFIGHTING.getFramesPerDir())
                    frame = 0;
                g.drawImage(SpriteSheet.WIZARDFIGHTING.getSprites().get(SpriteSheet.WIZARDFIGHTING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.WIZARDFIGHTING.getFramesPerDir();
                break;
            case IDLE:
                if (frame >= SpriteSheet.WIZARDIDEAL.getFramesPerDir())
                    frame = 0;
                g.drawImage(SpriteSheet.WIZARDIDEAL.getSprites().get(SpriteSheet.WIZARDIDEAL.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.WIZARDIDEAL.getFramesPerDir();
                break;
            default:
                break;
        }

    }
}
