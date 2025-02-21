package com.igormaznitsa.zxpoly.components.snd;

import com.igormaznitsa.zxpoly.components.IoDevice;
import com.igormaznitsa.zxpoly.components.Motherboard;
import com.igormaznitsa.zxpoly.components.ZxPolyModule;

public final class TurboSoundNedoPc implements IoDevice, SoundLevels {

  private final Motherboard motherboard;
  private final Beeper beeper;

  private final Ay8910Chip chipAy0;
  private final Ay8910Chip chipAy1;

  private Ay8910Chip selectedChip;

  public TurboSoundNedoPc(final Motherboard motherboard) {
    this.motherboard = motherboard;
    this.beeper = this.motherboard.getBeeper();
    this.chipAy0 = new Ay8910Chip(this::onLevels0);
    this.chipAy1 = new Ay8910Chip(this::onLevels1);

    this.doReset();
  }

  private void onLevels0(final Ay8910Chip ay, final int levelA, final int levelB,
                         final int levelC) {
    this.beeper.setChannelValue(Beeper.CHANNEL_AY_A, AMPLITUDE_16[levelA]);
    this.beeper.setChannelValue(Beeper.CHANNEL_AY_B, AMPLITUDE_16[levelB]);
    this.beeper.setChannelValue(Beeper.CHANNEL_AY_C, AMPLITUDE_16[levelC]);
  }

  private void onLevels1(final Ay8910Chip ay, final int levelA, final int levelB,
                         final int levelC) {
    this.beeper.setChannelValue(Beeper.CHANNEL_TS_A, AMPLITUDE_16[levelA]);
    this.beeper.setChannelValue(Beeper.CHANNEL_TS_B, AMPLITUDE_16[levelB]);
    this.beeper.setChannelValue(Beeper.CHANNEL_TS_C, AMPLITUDE_16[levelC]);
  }

  @Override
  public Motherboard getMotherboard() {
    return this.motherboard;
  }

  @Override
  public int readIo(final ZxPolyModule module, final int port) {
    if (!module.isTrdosActive() && (port & 2) == 0 && (port & 0x8000) == 0x8000) {
      return this.selectedChip.readData();
    }
    return -1;
  }

  @Override
  public void writeIo(final ZxPolyModule module, final int port, final int value) {
    if (!module.isTrdosActive() && (port & 2) == 0) {
      if ((port & 0xC000) == 0xC000) {
        if (value == 0xFF) {
          this.selectedChip = this.chipAy0;
        } else if (value == 0xFE) {
          this.selectedChip = this.chipAy1;
        }
        this.selectedChip.writeAddress(value);
      } else if ((port & 0xC000) == 0x8000) {
        this.selectedChip.writeData(value);
      }
    }
  }

  @Override
  public void preStep(
          final int frameTiStates,
          final boolean signalReset,
          final boolean tstatesIntReached,
          final boolean wallclockInt
  ) {
    if (signalReset) {
      this.doReset();
    }
  }

  @Override
  public void postStep(final int spentTstates) {
    this.chipAy0.step(spentTstates);
    this.chipAy1.step(spentTstates);
  }

  @Override
  public String getName() {
    return "TurboSound-NedoPc";
  }

  @Override
  public void doReset() {
    this.selectedChip = this.chipAy0;
    this.chipAy0.reset();
    this.chipAy1.reset();
  }

  @Override
  public int getNotificationFlags() {
    return NOTIFICATION_POSTSTEP | NOTIFICATION_PRESTEP;
  }
}
