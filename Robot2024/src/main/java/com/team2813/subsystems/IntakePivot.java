package com.team2813.subsystems;
import static com.team2813.Constants.INTAKE_ENCODER;
import static com.team2813.Constants.INTAKE_PIVOT;

import com.team2813.lib2813.control.ControlMode;
import com.team2813.lib2813.control.Encoder;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.Motor;
import com.team2813.lib2813.control.encoders.CancoderWrapper;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;
public class IntakePivot extends MotorSubsystem<IntakePivot.Rotations> {

    private static final double PIVOT_UP_SPEED = .10;
    private static final double PIVOT_DOWN_SPEED = -.10;
	private static final double error = 0.05;
    private Rotations currentPosition;
    
    Motor intakePivotMotor; 
    Encoder intakePivotEncoder;

    public IntakePivot() {
        
        super(new MotorSubsystemConfiguration(
			new TalonFXWrapper(INTAKE_PIVOT, InvertType.COUNTER_CLOCKWISE),
			new CancoderWrapper(INTAKE_ENCODER)
			).acceptableError(error));

        intakePivotMotor = new TalonFXWrapper(INTAKE_PIVOT, InvertType.CLOCKWISE);
        setSetpoint(Rotations.INTAKE_DOWN);
    }


    public void pivotUp() { intakePivotMotor.set(ControlMode.DUTY_CYCLE, PIVOT_UP_SPEED); }

    public void pivotDown() { intakePivotMotor.set(ControlMode.DUTY_CYCLE, PIVOT_DOWN_SPEED); }

    public void stopPivotMotor() { intakePivotMotor.set(ControlMode.DUTY_CYCLE, 0); }

    @Override
    public void setSetpoint(Rotations setPoint) {
        super.setSetpoint(setPoint);
        currentPosition = setPoint;
    }
    
    public boolean positionReached() {
        return Math.abs(currentPosition.getPos() - getMeasurement()) < 0.05;
    }

    public static enum Rotations implements MotorSubsystem.Position {
		INTAKE_DOWN(-6.846191),
        INTAKE_UP(0.070312);

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


