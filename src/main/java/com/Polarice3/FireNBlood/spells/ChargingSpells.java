package com.Polarice3.FireNBlood.spells;

public abstract class ChargingSpells extends Spells{

    public abstract int Cooldown();

    public int CastDuration() {
        return 72000;
    }

}
