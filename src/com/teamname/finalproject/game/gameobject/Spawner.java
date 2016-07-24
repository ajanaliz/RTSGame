package com.teamname.finalproject.game.gameobject;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.gameobject.particle.Particle;
import com.teamname.finalproject.game.gameobject.particle.SmartParticles;
import com.teamname.finalproject.game.gameobject.projectile.Spell;

import java.awt.*;

/**
 * Created by Ali J on 6/24/2015.
 */
public class Spawner extends GameObject {


    private static final Color explosionColor = new Color(255, 220, 16);
    private static final Color bloodColor = new Color(105, 0, 4);


    public Spawner(double x, double y, ObjectType type, int amount, int direction) {
        super(x, y, type);
        double random = Math.random();
        for (int i = 0; i < amount; i++) {
            switch (type) {
                case EXPLOSION_PARTICLE:
                    Particle explosionParticle = new Particle(x, y, ObjectType.PARTICLE, 44, 10, explosionColor);
                    Game.getLevel().getParticles().add(explosionParticle);
                    break;
                case BLOOD_PARTICLE:
                    Particle bloodParticle = new SmartParticles(x, y, ObjectType.PARTICLE, 44, 4, bloodColor, direction);
                    Game.getLevel().getParticles().add(bloodParticle);
                    break;
                case FIRE_SPELL_PARTICLE:
                    if (random > 0.666) {
                        Particle fireSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.FIRESPELLCOLOR1);
                        Game.getLevel().getParticles().add(fireSpellExplosion);
                    } else if (random > 0.333) {
                        Particle fireSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.FIRESPELLCOLOR2);
                        Game.getLevel().getParticles().add(fireSpellExplosion);
                    } else {
                        Particle fireSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.FIRESPELLCOLOR3);
                        Game.getLevel().getParticles().add(fireSpellExplosion);
                    }
                    break;
                case WATER_SPELL_PARTICLE:
                    if (random > 0.666) {
                        Particle waterSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.WATERSPELLCOLOR1);
                        Game.getLevel().getParticles().add(waterSpellExplosion);
                    } else if (random > 0.333) {
                        Particle waterSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.WATERSPELLCOLOR2);
                        Game.getLevel().getParticles().add(waterSpellExplosion);
                    } else {
                        Particle waterSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.WATERSPELLCOLOR3);
                        Game.getLevel().getParticles().add(waterSpellExplosion);
                    }
                    break;
                case EARTH_SPELL_PARTICLE:
                    if (random > 0.666) {
                        Particle earthSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.EARTHSPELLCOLOR1);
                        Game.getLevel().getParticles().add(earthSpellExplosion);
                    } else if (random > 0.333) {
                        Particle earthSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.EARTHSPELLCOLOR2);
                        Game.getLevel().getParticles().add(earthSpellExplosion);
                    } else {
                        Particle earthSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.EARTHSPELLCOLOR3);
                        Game.getLevel().getParticles().add(earthSpellExplosion);
                    }
                    break;
                case ICE_SPELL_PARTICLE:
                    if (random > 0.666) {
                        Particle iceSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.ICESPELLCOLOR1);
                        Game.getLevel().getParticles().add(iceSpellExplosion);
                    } else if (random > 0.333) {
                        Particle iceSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.ICESPELLCOLOR2);
                        Game.getLevel().getParticles().add(iceSpellExplosion);
                    } else {
                        Particle iceSpellExplosion = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.ICESPELLCOLOR3);
                        Game.getLevel().getParticles().add(iceSpellExplosion);
                    }
                    break;
                case FIRE_SPELL_FLARE:
                    if (random > 0.666) {
                        Particle fireSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.FIRESPELLCOLOR1);
                        Game.getLevel().getParticles().add(fireSpellFlare);
                    } else if (random > 0.333) {
                        Particle fireSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.FIRESPELLCOLOR2);
                        Game.getLevel().getParticles().add(fireSpellFlare);
                    } else {
                        Particle fireSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 44, 5, Spell.FIRESPELLCOLOR3);
                        Game.getLevel().getParticles().add(fireSpellFlare);
                    }
                    break;
                case WATER_SPELL_FLARE:
                    if (random > 0.666) {
                        Particle waterSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 5, Spell.WATERSPELLCOLOR1);
                        Game.getLevel().getParticles().add(waterSpellFlare);
                    } else if (random > 0.333) {
                        Particle waterSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 5, Spell.WATERSPELLCOLOR2);
                        Game.getLevel().getParticles().add(waterSpellFlare);
                    } else {
                        Particle waterSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 5, Spell.WATERSPELLCOLOR3);
                        Game.getLevel().getParticles().add(waterSpellFlare);
                    }
                    break;
                case ICE_SPELL_FLARE:
                    if (random > 0.666) {
                        Particle iceSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 3, Spell.ICESPELLCOLOR1);
                        Game.getLevel().getParticles().add(iceSpellFlare);
                    } else if (random > 0.333) {
                        Particle iceSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 3, Spell.ICESPELLCOLOR2);
                        Game.getLevel().getParticles().add(iceSpellFlare);
                    } else {
                        Particle iceSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 3, Spell.ICESPELLCOLOR3);
                        Game.getLevel().getParticles().add(iceSpellFlare);
                    }
                    break;
                case EARTH_SPELL_FLARE:
                    if (random > 0.666) {
                        Particle earthSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 3, Spell.EARTHSPELLCOLOR1);
                        Game.getLevel().getParticles().add(earthSpellFlare);
                    } else if (random > 0.333) {
                        Particle earthSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 3, Spell.EARTHSPELLCOLOR2);
                        Game.getLevel().getParticles().add(earthSpellFlare);
                    } else {
                        Particle earthSpellFlare = new Particle(x, y, ObjectType.PARTICLE, 22, 3, Spell.EARTHSPELLCOLOR3);
                        Game.getLevel().getParticles().add(earthSpellFlare);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void render(Graphics g) {

    }
}
