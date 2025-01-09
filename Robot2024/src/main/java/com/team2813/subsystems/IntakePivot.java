package com.team2813.subsystems;
import static com.team2813.Constants.INTAKE_ENCODER;
import static com.team2813.Constants.INTAKE_PIVOT;

import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team2813.lib2813.control.InvertType;
import com.team2813.lib2813.control.PIDMotor;
import com.team2813.lib2813.control.encoders.CancoderWrapper;
import com.team2813.lib2813.control.motors.TalonFXWrapper;
import com.team2813.lib2813.subsystems.MotorSubsystem;

import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.Supplier;

public class IntakePivot extends MotorSubsystem<IntakePivot.Rotations> {

    public IntakePivot() {
        
        super(new MotorSubsystemConfiguration(
			pivotMotor(),
			new CancoderWrapper(INTAKE_ENCODER)
			)
			.PID(0.313, 0, 0)
			.acceptableError(0.5)
			.startingPosition(Rotations.INTAKE_UP));
    }

	public void resetPosition() {
		encoder.setPosition(0);
	}

	@Override
	protected void useOutput(double output, double setpoint) {
		if (output < 0) {
			output -= 0.02;
		}
		super.useOutput(output, setpoint);
	}
	
	private static PIDMotor pivotMotor() {
		TalonFXWrapper pivotMotor = new TalonFXWrapper(INTAKE_PIVOT, InvertType.CLOCKWISE);
		pivotMotor.setNeutralMode(NeutralModeValue.Brake);

		return pivotMotor;
	}

	@Override
	public void periodic() {
		super.periodic();
		SmartDashboard.putNumber("Intake Pivot CANCoder Position", encoder.position());
	}

    public enum Rotations implements Supplier<Measure<Angle>> {
		INTAKE_DOWN(0.825439),
		INTAKE_UP(0);

        Rotations(double pos) {
            this.pos = pos;
        }

        private final double pos;

		@Deprecated
		public double getPos() {
			return pos;
		}
		
		@Override
		public Measure<Angle> get() {
			return Units.Rotations.of(pos);
		}
	}

}


