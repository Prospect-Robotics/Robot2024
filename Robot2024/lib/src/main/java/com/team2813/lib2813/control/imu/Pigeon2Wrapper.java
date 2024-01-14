package com.team2813.lib2813.control.imu;

import com.ctre.phoenix.sensors.Pigeon2;
import com.ctre.phoenix.sensors.Pigeon2Configuration;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;
import com.team2813.lib2813.util.ConfigUtils;
import com.team2813.lib2813.util.Port;

public class Pigeon2Wrapper extends Pigeon2 {

    private double currentHeading = 0;

    private final boolean canivore;

    /**
     * Constructor
     * @param port The port to configure.
     * @param canbus Name of the CANbus; can be a SocketCAN interface (on Linux),
     *               or a CANivore device name or serial number
     */
    public Pigeon2Wrapper(Port port, String canbus) {
        super(port.getCanId(), canbus);
        canivore = true;

        ConfigUtils.ctreConfig(() -> configAllSettings(new Pigeon2Configuration()));
    }

    /**
     * Constructor
     * @param The port to configure.
     */
    public Pigeon2Wrapper(Port port) {
        super(port.getCanId());
        canivore = false;

        ConfigUtils.ctreConfig(() -> configAllSettings(new Pigeon2Configuration()));
        ConfigUtils.ctreConfig(
                () -> setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 20)
        );
    }

    public double getHeading() {
        return getYaw();
    }

    public void setHeading(double angle) {
        setYaw(angle);
        setAccumZAngle(0);

        currentHeading = angle;
    }

    /**
     * Checks if a reset has occurred and restores non-persistent settings if so.
     * Implement periodically (e.g. in a subsystem's periodic() method)
     */
    public void periodicResetCheck() {
        if (!hasResetOccurred()) {
            currentHeading = getHeading();
        }
        else {
            if (!canivore) {
                ConfigUtils.ctreConfig(
                        () -> setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 20)
                );
            }
            setHeading(currentHeading);
        }
    }
}