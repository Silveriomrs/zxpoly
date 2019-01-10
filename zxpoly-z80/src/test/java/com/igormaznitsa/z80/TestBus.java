/* 
 * Copyright (C) 2014 Igor Maznitsa (http://www.igormaznitsa.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.igormaznitsa.z80;

import static org.junit.Assert.assertFalse;

public final class TestBus implements Z80CPUBus {

  private final byte [] memory  = new byte[0x10000];
  private final byte [] ports  = new byte[0x10000];
  private final byte dataBuSState;
  private boolean reti;
  
  public TestBus(final int busState, final int address, final int... codes){
    block(address, codes);
    this.dataBuSState = (byte) busState;
  }
  
  public void block(final int address, final int ... codes){
    int pc = address;
    for (final int c : codes) {
      this.memory[pc & 0xFFFF] = (byte) c;
      pc++;
    }
  }
  
  public void resetRETIFlag(){
    this.reti = false;
  }
  
  public boolean isRETI(){
    return this.reti;
  }
  
  @Override
  public byte readMemory(final Z80 cpu, final int address, boolean m1) {
    return memory[address];
  }

  @Override
  public void writeMemory(final Z80 cpu, final int address, final byte data) {
    this.memory [address] = data;
  }

  @Override
  public byte readPort(final  Z80 cpu, final int port) {
    return this.ports [port & 0xFFFF];
  }

  @Override
  public void writePort(final Z80 cpu, final int port, final byte data) {
    this.ports [port & 0xFFFF] = data;
  }

  @Override
  public byte onCPURequestDataLines(final Z80 cpu) {
    return this.dataBuSState;
  }
  
  @Override
  public void onRETI(Z80 cpu) {
    assertFalse("RETI flag must be reset", this.reti);
    this.reti = true;
  }
}
