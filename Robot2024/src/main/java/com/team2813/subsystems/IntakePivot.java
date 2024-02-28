package com.team2813.subsystems;
import static com.team2813.Constants.INTAKE_ENCODER;
import static com.team2813.Constants.INTAKE_PIVOT;

import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team2813.lib2813.control.Encoder;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.encoders.CancoderWrapper;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;
public class IntakePivot extends MotorSubsystem<IntakePivot.Rotations> {
	private static final double error = 0.5;
    private Rotations currentPosition;
    
    Motor intakePivotMotor; 
    Encoder intakePivotEncoder;

    public IntakePivot() {
        
        super(new MotorSubsystemConfiguration(
			pivotMotor(),
			new CancoderWrapper(INTAKE_ENCODER)
			)
			.PID(0.315, 0, 0)
			.acceptableError(error)
			.startingPosition(Rotations.INTAKE_UP));

        intakePivotMotor = new TalonFXWrapper(INTAKE_PIVOT, InvertType.CLOCKWISE);
    }
	
	private static PIDMotor pivotMotor() {
		TalonFXWrapper pivotMotor = new TalonFXWrapper(INTAKE_PIVOT, InvertType.CLOCKWISE);
		pivotMotor.setNeutralMode(NeutralModeValue.Brake);
		return pivotMotor;
	}

    @Override
    public void setSetpoint(Rotations setPoint) {
        super.setSetpoint(setPoint);
        currentPosition = setPoint;
    }
    
    public boolean positionReached() {
        return Math.abs(currentPosition.getPos() - getMeasurement()) < 0.05;
    }

    public static enum Rotations implements MotorSubsystem.Position {
		INTAKE_DOWN(-0.782471),
        INTAKE_UP(-0.000732);

        Rotations(double pos) {
            this.pos = pos;
        }

        private final double pos;
        @Override
		public double getPos() {
			return pos;
		}
    }

}


