package com.team2813.lib2813.swerve.controllers.steer;

import com.ctre.phoenix.sensors.CANCoder;
import com.swervedrivespecialties.swervelib.AbsoluteEncoder;

public class SteerEncoder implements AbsoluteEncoder {
    private CANCoder unlicensedEncoder;

    public SteerEncoder(CANCoder unlicensedEncoder) {
        this.unlicensedEncoder = unlicensedEncoder;
    }

    @Override
    public double getAbsoluteAngle() {
        double angle;
		angle = Math.toRadians(unlicensedEncoder.getAbsolutePosition());
        angle %= 2.0 * Math.PI;
        if (angle < 0.0) {
            angle += 2.0 * Math.PI;
        }

        return angle;
    }
}