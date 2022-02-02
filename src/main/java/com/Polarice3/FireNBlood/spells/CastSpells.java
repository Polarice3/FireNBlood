package com.Polarice3.FireNBlood.spells;

public class CastSpells {
    private final int spellint;

    public CastSpells(int spellint){
        this.spellint = spellint;
    }

    public Spells getSpell(){
        switch (spellint){
            case 0:
                return new VexSpell();
            case 1:
                return new FangSpell();
            case 2:
                return new RoarSpell();
            case 3:
                return new ZombieSpell();
            case 4:
                return new SkeletonSpell();
            case 5:
                return new CrippleSpell();
            case 6:
                return new SpiderlingSpell();
            case 7:
                return new BrainEaterSpell();
            default:
                return null;
        }
    }
}
