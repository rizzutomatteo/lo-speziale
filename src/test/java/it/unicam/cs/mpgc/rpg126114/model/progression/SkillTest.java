package it.unicam.cs.mpgc.rpg126114.model.progression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillTest {

    @Test
    void enoughExperienceRaisesTheLevel() {
        Skill skill = new Skill(SkillType.DISTILLAZIONE);
        skill.gainExperience(skill.getExperienceForNextLevel());
        assertEquals(2, skill.getLevel());
        assertEquals(0, skill.getExperience());
    }

    @Test
    void experienceCarriesAcrossSeveralLevels() {
        Skill skill = new Skill(SkillType.DIAGNOSI);
        skill.gainExperience(150); // 50 to reach level 2, then 100 to reach level 3
        assertEquals(3, skill.getLevel());
    }

    @Test
    void experienceStopsAccumulatingAtTheMaximumLevel() {
        Skill skill = new Skill(SkillType.ERBORISTERIA, Skill.MAX_LEVEL, 0);
        skill.gainExperience(1000);
        assertTrue(skill.isMaxed());
        assertEquals(Skill.MAX_LEVEL, skill.getLevel());
    }
}
