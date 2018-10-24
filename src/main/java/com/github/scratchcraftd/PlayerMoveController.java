package com.github.scratchcraftd;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;

public class PlayerMoveController {

    private static final int PLAYER_WALK_STEP_DURATION = 500;
    private static MovementInput prevIn;

    public static double getYawFromOriginToPoint(double x, double y) {
        if (x == 0 && y == 0) {
            return 0;
        }
        double ans = 90 - Math.toDegrees(Math.atan2(y, x));
        return ans;
    }

    public static double getPitchFromOriginToPoint(double x, double y) {
        if (y == 0) {
            return 0;
        }
        double ans = Math.toDegrees(Math.atan2(y, x));
        if (ans > 90 || ans < -90) {
            if (y < 0) {
                ans = 180 + ans;
                ans = -ans;
            } else {
                ans = 180 - ans;
            }
        }
        return -ans;
    }

    public EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().player;
    }

    public boolean moveStraightTo(BlockPos pos) {
        return moveStraightTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
    }

    public boolean moveStraightTo(double x, double y, double z) {
        return moveStraightTo(x, y, z, true);
    }

    public boolean moveStraightTo(final double x, final double y, final double z, boolean autoFace) {
        System.out.println("Movin to " + x + "," + y + "," + z);
        if (autoFace) {
            faceTowards(x, y + getPlayer().getEyeHeight(), z);
        }
        MovementInput m = new MovementInput() {
            @Override
            public void updatePlayerMoveState() {
                super.updatePlayerMoveState();
                if ((int) (getPlayer().posX) == (int) x && (int) (getPlayer().posY) == (int) y && (int) (getPlayer().posZ) == (int) z) {
                    resetPlayerInput();
                    System.out.println("Player Reached");
                }
            }
        };
        m.forwardKeyDown = true;
        m.moveForward = 1;
        setPlayerInput(m);
        return true;
    }

    public boolean faceTowards(double x, double y, double z) {
        double yaw, pitch;
        System.out.println("Facin to " + x + "," + y + "," + z);
        System.out.println("My Pos " + getPlayer().posX + "," + getPlayer().posY + "," + getPlayer().posZ);

        x -= getPlayer().posX;
        x = -x;
        y = y - (getPlayer().posY + getPlayer().getEyeHeight());
        z -= getPlayer().posZ;
        System.out.println("My dist " + x + "," + y + "," + z);
        yaw = getYawFromOriginToPoint(x, z);

        if (y == 0) {
            pitch = 0;
        } else {
            pitch = getPitchFromOriginToPoint(Math.sqrt((x * x) + (z * z)), y);
        }
        System.out.println("Pitch : " + pitch);
        System.out.println("Yaw : " + yaw);
        setAngles((float) yaw, (float) pitch);
        return true;
    }

    public void forward(final int steps) {
        final long start = System.currentTimeMillis();

        MovementInput m = new MovementInput() {
            @Override
            public void updatePlayerMoveState() {
                super.updatePlayerMoveState();
                if (System.currentTimeMillis() - start > (steps * PLAYER_WALK_STEP_DURATION)) {
                    resetPlayerInput();
                }
            }
        };

        m.forwardKeyDown = true;
        m.moveForward = 1;
        setPlayerInput(m);
    }

    public void backward(final int steps) {
        final long start = System.currentTimeMillis();

        MovementInput m = new MovementInput() {
            @Override
            public void updatePlayerMoveState() {
                super.updatePlayerMoveState();
                if (System.currentTimeMillis() - start > (steps * PLAYER_WALK_STEP_DURATION)) {
                    resetPlayerInput();
                }
            }
        };
        m.backKeyDown = true;
        m.moveForward = -1;
        setPlayerInput(m);
    }

    public void left(final int steps) {
        final long start = System.currentTimeMillis();

        MovementInput m = new MovementInput() {
            @Override
            public void updatePlayerMoveState() {
                super.updatePlayerMoveState();
                if (System.currentTimeMillis() - start > (steps * PLAYER_WALK_STEP_DURATION)) {
                    resetPlayerInput();
                }
            }
        };
        m.leftKeyDown = true;
        m.moveStrafe = 1;
        setPlayerInput(m);
    }

    public void right(final int steps) {
        final long start = System.currentTimeMillis();

        MovementInput m = new MovementInput() {
            @Override
            public void updatePlayerMoveState() {
                super.updatePlayerMoveState();
                if (System.currentTimeMillis() - start > (steps * PLAYER_WALK_STEP_DURATION)) {
                    resetPlayerInput();
                }
            }
        };
        m.rightKeyDown = true;
        m.moveStrafe = -1;
        setPlayerInput(m);
    }

    public void setAngles(float yaw, float pitch) {
        getPlayer().rotationYaw = yaw;
        getPlayer().rotationYawHead = yaw;
        System.out.println("Yaw set to : " + yaw);
        getPlayer().rotationPitch = pitch;
        System.out.println("Pitch set to : " + pitch);
    }

    public void resetPlayerInput() {
        System.out.println("Player Input Reset");
        getPlayer().movementInput = prevIn;
        prevIn = null;
    }

    public void setPlayerInput(MovementInput i) {
        if (prevIn == null) {
            prevIn = getPlayer().movementInput;
        }
        getPlayer().movementInput = i;
    }
}