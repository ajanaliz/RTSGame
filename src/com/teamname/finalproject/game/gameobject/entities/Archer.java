package com.teamname.finalproject.game.gameobject.entities;

import java.awt.Graphics;
import java.util.Random;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.projectile.Projectile;
import com.teamname.finalproject.game.gameobject.projectile.Spell;

public class Archer extends RangedFighter {

	public Archer(double x, double y, ObjectType type, PlayerID myOwner,
			int MAXHEALTH, Kingdom kingdom, int healPerTick, int fightrange) {
		super(x, y, type, myOwner, MAXHEALTH, kingdom, healPerTick, fightrange);
		 
	}
	@Override
	public void shoot(double dir) {

		Random random = new Random();
		int rand = random.nextInt(4);
		switch (rand) {
		case 0:
			Projectile p = new Spell(x,y,ObjectType.FIRE_SPELL,dir , getDirection()  , this);
			 Game.getLevel().getProjectiles().add(p);

			break;
		case 1:
			Projectile z = new Spell(x,y,ObjectType.WATER_SPELL,dir , getDirection(), this);
			 Game.getLevel().getProjectiles().add(z);

			break;
		case 2:
			Projectile s = new Spell(x,y,ObjectType.ICE_SPELL,dir , getDirection(), this);
			 Game.getLevel().getProjectiles().add(s);

			break;
		
		default:
			Projectile c = new Spell(x,y,ObjectType.EARTH_SPELL,dir , getDirection(), this);
			 Game.getLevel().getProjectiles().add(c);

			break;
		}
		
	}
	
	@Override
	public void render(Graphics g) {
	
//		 switch (loopUpStates(state.getStateID())) {
//			case MOVE:
//				g.drawImage(SpriteSheet.KINGWALKING.getSprites().get(15 * getDirection() + frame),(int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
//	    	if(frame>=SpriteSheet.KINGWALKING.getFramesPerDir() )
//	    		frame=0;
//		 if (time % 5 == 0)
//	    		 frame = (frame + 1) % SpriteSheet.KINGWALKING.getFramesPerDir();
//				break;
//			case MOVETOFERRY:
//	    	if(frame>=SpriteSheet.KINGWALKING.getFramesPerDir())
//	    		frame=0;
//				g.drawImage(SpriteSheet.KINGWALKING.getSprites().get(15 * getDirection() + frame),(int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
//	    		 if (time % 5 == 0)
//	    		 frame = (frame + 1) % 36;
//				
//				break;
//				
//			case MOVETOFIGHT:
//	    	if(frame>=SpriteSheet.KINGWALKTOFIGHT.getFramesPerDir())
//	    		frame=0;
//				g.drawImage(SpriteSheet.KINGWALKTOFIGHT.getSprites().get(15 * getDirection() + frame),(int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
//	    		 if (time % 5 == 0)
//	    		 frame = (frame + 1) % SpriteSheet.KINGWALKTOFIGHT.getFramesPerDir();
//
//				break;
//				
//			case  FIGHTING:
//	    	if(frame>=SpriteSheet.KINGFIGHTING.getFramesPerDir())
//	    		frame=0;
//				g.drawImage(SpriteSheet.KINGFIGHTING.getSprites().get(15 * getDirection() + frame),(int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
//	    		 if (time % 5 == 0)
//	    		 frame = (frame + 1) %  SpriteSheet.KINGFIGHTING.getFramesPerDir();
//				break;
//				
//			case IDLE :
//	    	if(frame>=SpriteSheet.KINGIDEAL.getFramesPerDir())
//	    		frame=0;
//	    	
//	    	
//				g.drawImage(SpriteSheet.KINGIDEAL.getSprites().get(15 * getDirection() + frame),(int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
//	    		 if (time % 5 == 0)
//	    		 frame = (frame + 1) % SpriteSheet.KINGIDEAL.getFramesPerDir() ;
//	    		 
//				break;
//			
//			default:
//				break;
//			}
	}
}
