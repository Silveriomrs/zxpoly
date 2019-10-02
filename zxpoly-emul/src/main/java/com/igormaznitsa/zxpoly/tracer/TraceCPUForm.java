/*
 * Copyright (C) 2014-2019 Igor Maznitsa
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

package com.igormaznitsa.zxpoly.tracer;

import com.igormaznitsa.z80.MemoryAccessProvider;
import com.igormaznitsa.z80.Z80;
import com.igormaznitsa.z80.Z80Instruction;
import com.igormaznitsa.z80.disasm.Z80Disasm;
import com.igormaznitsa.zxpoly.MainForm;
import com.igormaznitsa.zxpoly.components.Motherboard;
import com.igormaznitsa.zxpoly.components.ZxPolyModule;
import java.awt.Component;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("unchecked")
public class TraceCPUForm extends javax.swing.JFrame implements MemoryAccessProvider {

  private final MainForm mainForm;
  private final Motherboard motherboard;
  private final ZxPolyModule module;
  private final int moduleIndex;
  private final StringBuilder buffer = new StringBuilder(32);

  private boolean changeEnabled = true;
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton buttonMemory;
  private javax.swing.JCheckBox checkBoxC;
  private javax.swing.JCheckBox checkBoxF3;
  private javax.swing.JCheckBox checkBoxF5;
  private javax.swing.JCheckBox checkBoxH;
  private javax.swing.JCheckBox checkBoxIFF1;
  private javax.swing.JCheckBox checkBoxIFF2;
  private javax.swing.JCheckBox checkBoxN;
  private javax.swing.JCheckBox checkBoxPV;
  private javax.swing.JCheckBox checkBoxS;
  private javax.swing.JCheckBox checkBoxZ;
  private javax.swing.JList disasmList;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldAltRegA;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldAltRegB;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldAltRegC;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldAltRegD;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldAltRegE;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldAltRegF;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldAltRegH;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldAltRegL;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldIM;
  private com.igormaznitsa.zxpoly.tracer.HexValue4Field fieldIX;
  private com.igormaznitsa.zxpoly.tracer.HexValue4Field fieldIY;
  private com.igormaznitsa.zxpoly.tracer.HexValue4Field fieldPC;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldR;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldRegA;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldRegB;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldRegC;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldRegD;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldRegE;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldRegF;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldRegH;
  private com.igormaznitsa.zxpoly.tracer.HexValue2Field fieldRegL;
  private com.igormaznitsa.zxpoly.tracer.HexValue4Field fieldSP;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel18;
  private javax.swing.JLabel jLabel19;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel20;
  private javax.swing.JLabel jLabel21;
  private javax.swing.JLabel jLabel22;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel panelAltRegSet;
  private javax.swing.JPanel panelCommonRegisters;
  private javax.swing.JPanel panelFlags;
  private javax.swing.JPanel panelRegSet;
  public TraceCPUForm(final MainForm mainForm, final Motherboard motherboard, final int moduleIndex) {
    initComponents();
    this.moduleIndex = moduleIndex;
    this.mainForm = mainForm;
    this.motherboard = motherboard;
    this.module = this.motherboard.getModules()[moduleIndex];
    this.setTitle("Tracing module #" + moduleIndex);

    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    if (this.changeEnabled) {
      this.changeEnabled = false;
      setEnableForComponentsOfPanel(this.panelAltRegSet, this.changeEnabled);
      setEnableForComponentsOfPanel(this.panelCommonRegisters, this.changeEnabled);
      setEnableForComponentsOfPanel(this.panelFlags, this.changeEnabled);
      setEnableForComponentsOfPanel(this.panelRegSet, this.changeEnabled);
    }

    this.setLocationByPlatform(true);

    pack();
  }

  private static void int2hex4(final StringBuilder buffer, final int value) {
    final String str = Integer.toHexString(value).toUpperCase(Locale.ENGLISH);
    if (str.length() < 4) {
      for (int i = 0; i < 4 - str.length(); i++) {
        buffer.append('0');
      }
    }
    buffer.append(str);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    panelFlags = new javax.swing.JPanel();
    checkBoxS = new javax.swing.JCheckBox();
    checkBoxZ = new javax.swing.JCheckBox();
    checkBoxF5 = new javax.swing.JCheckBox();
    checkBoxH = new javax.swing.JCheckBox();
    checkBoxF3 = new javax.swing.JCheckBox();
    checkBoxPV = new javax.swing.JCheckBox();
    checkBoxN = new javax.swing.JCheckBox();
    checkBoxC = new javax.swing.JCheckBox();
    panelRegSet = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    fieldRegA = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldRegC = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldRegD = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldRegE = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldRegB = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldRegF = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    jLabel19 = new javax.swing.JLabel();
    fieldRegH = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    jLabel20 = new javax.swing.JLabel();
    fieldRegL = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    panelCommonRegisters = new javax.swing.JPanel();
    fieldPC = new com.igormaznitsa.zxpoly.tracer.HexValue4Field();
    jLabel7 = new javax.swing.JLabel();
    fieldSP = new com.igormaznitsa.zxpoly.tracer.HexValue4Field();
    jLabel8 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    fieldIX = new com.igormaznitsa.zxpoly.tracer.HexValue4Field();
    jLabel10 = new javax.swing.JLabel();
    fieldIY = new com.igormaznitsa.zxpoly.tracer.HexValue4Field();
    jLabel11 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    fieldR = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldIM = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    checkBoxIFF1 = new javax.swing.JCheckBox();
    checkBoxIFF2 = new javax.swing.JCheckBox();
    panelAltRegSet = new javax.swing.JPanel();
    jLabel13 = new javax.swing.JLabel();
    jLabel14 = new javax.swing.JLabel();
    jLabel15 = new javax.swing.JLabel();
    jLabel16 = new javax.swing.JLabel();
    jLabel17 = new javax.swing.JLabel();
    jLabel18 = new javax.swing.JLabel();
    fieldAltRegA = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldAltRegC = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldAltRegD = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldAltRegE = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldAltRegB = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    fieldAltRegF = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    jLabel21 = new javax.swing.JLabel();
    fieldAltRegH = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    jLabel22 = new javax.swing.JLabel();
    fieldAltRegL = new com.igormaznitsa.zxpoly.tracer.HexValue2Field();
    jPanel3 = new javax.swing.JPanel();
    disasmList = new javax.swing.JList();
    buttonMemory = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);
    addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosed(java.awt.event.WindowEvent evt) {
        formWindowClosed(evt);
      }

      @Override
      public void windowActivated(java.awt.event.WindowEvent evt) {
        formWindowActivated(evt);
      }
    });

    panelFlags.setBorder(javax.swing.BorderFactory.createTitledBorder("Flags"));

    checkBoxS.setText("S");
    checkBoxS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    checkBoxS.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    panelFlags.add(checkBoxS);

    checkBoxZ.setText("Z");
    checkBoxZ.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    checkBoxZ.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    checkBoxZ.addActionListener(this::checkBoxZActionPerformed);
    panelFlags.add(checkBoxZ);

    checkBoxF5.setText("F5");
    checkBoxF5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    checkBoxF5.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    panelFlags.add(checkBoxF5);

    checkBoxH.setText("H");
    checkBoxH.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    checkBoxH.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    panelFlags.add(checkBoxH);

    checkBoxF3.setText("F3");
    checkBoxF3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    checkBoxF3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    panelFlags.add(checkBoxF3);

    checkBoxPV.setText("P/V");
    checkBoxPV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    checkBoxPV.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    panelFlags.add(checkBoxPV);

    checkBoxN.setText("N");
    checkBoxN.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    checkBoxN.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    panelFlags.add(checkBoxN);

    checkBoxC.setText("C");
    checkBoxC.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    checkBoxC.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    panelFlags.add(checkBoxC);

    panelRegSet.setBorder(javax.swing.BorderFactory.createTitledBorder("Reg.set"));

    jLabel1.setText("A:");

    jLabel2.setText("F:");

    jLabel3.setText("B:");

    jLabel4.setText("C:");

    jLabel5.setText("D:");

    jLabel6.setText("E:");

    fieldRegA.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldRegA.setText("FF");
    fieldRegA.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldRegC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldRegC.setText("FF");
    fieldRegC.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldRegD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldRegD.setText("FF");
    fieldRegD.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldRegE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldRegE.setText("FF");
    fieldRegE.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldRegB.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldRegB.setText("FF");
    fieldRegB.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldRegF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldRegF.setText("FF");
    fieldRegF.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    jLabel19.setText("H:");

    fieldRegH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldRegH.setText("FF");
    fieldRegH.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    jLabel20.setText("L:");

    fieldRegL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldRegL.setText("FF");
    fieldRegL.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    javax.swing.GroupLayout panelRegSetLayout = new javax.swing.GroupLayout(panelRegSet);
    panelRegSet.setLayout(panelRegSetLayout);
    panelRegSetLayout.setHorizontalGroup(
        panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegSetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegSetLayout.createSequentialGroup()
                        .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldRegD, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(fieldRegE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fieldRegH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fieldRegL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelRegSetLayout.createSequentialGroup()
                        .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fieldRegA, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                            .addComponent(fieldRegF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelRegSetLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldRegB, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegSetLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldRegC, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
    );

    panelRegSetLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, fieldRegA, fieldRegB, fieldRegC, fieldRegD, fieldRegE, fieldRegF);

    panelRegSetLayout.setVerticalGroup(
        panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegSetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(fieldRegA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(fieldRegF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(fieldRegB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(fieldRegC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(fieldRegD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(fieldRegE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel19)
                    .addComponent(fieldRegH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel20)
                    .addComponent(fieldRegL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    panelCommonRegisters.setBorder(javax.swing.BorderFactory.createTitledBorder("Common registers"));

    fieldPC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldPC.setText("FFFF");
    fieldPC.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    jLabel7.setText("PC:");

    fieldSP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldSP.setText("FFFF");
    fieldSP.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    jLabel8.setText("SP:");

    jLabel9.setText("IX:");

    fieldIX.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldIX.setText("FFFF");
    fieldIX.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    jLabel10.setText("IY:");

    fieldIY.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldIY.setText("FFFF");
    fieldIY.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    jLabel11.setText("R:");

    jLabel12.setText("I:");

    fieldR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldR.setText("FF");
    fieldR.setToolTipText("");
    fieldR.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldIM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldIM.setText("FF");
    fieldIM.setToolTipText("");
    fieldIM.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    checkBoxIFF1.setText("IFF1");

    checkBoxIFF2.setText("IFF2");

    javax.swing.GroupLayout panelCommonRegistersLayout = new javax.swing.GroupLayout(panelCommonRegisters);
    panelCommonRegisters.setLayout(panelCommonRegistersLayout);
    panelCommonRegistersLayout.setHorizontalGroup(
        panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCommonRegistersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCommonRegistersLayout.createSequentialGroup()
                        .addComponent(checkBoxIFF1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkBoxIFF2))
                    .addGroup(panelCommonRegistersLayout.createSequentialGroup()
                        .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel12)
                                .addGroup(panelCommonRegistersLayout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(fieldPC, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel11)))
                            .addGroup(panelCommonRegistersLayout.createSequentialGroup()
                                .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldIX, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldIY, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldSP, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldR, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldIM, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    panelCommonRegistersLayout.setVerticalGroup(
        panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCommonRegistersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldPC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11)
                    .addComponent(fieldR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel12)
                    .addComponent(fieldIM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldIX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldIY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(panelCommonRegistersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBoxIFF1)
                    .addComponent(checkBoxIFF2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    panelAltRegSet.setBorder(javax.swing.BorderFactory.createTitledBorder("Alt.reg.set"));

    jLabel13.setText("A:");

    jLabel14.setText("F:");

    jLabel15.setText("B:");

    jLabel16.setText("C:");

    jLabel17.setText("D:");

    jLabel18.setText("E:");

    fieldAltRegA.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldAltRegA.setText("FF");
    fieldAltRegA.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldAltRegC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldAltRegC.setText("FF");
    fieldAltRegC.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldAltRegD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldAltRegD.setText("FF");
    fieldAltRegD.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldAltRegE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldAltRegE.setText("FF");
    fieldAltRegE.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldAltRegB.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldAltRegB.setText("FF");
    fieldAltRegB.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    fieldAltRegF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldAltRegF.setText("FF");
    fieldAltRegF.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    jLabel21.setText("H:");

    fieldAltRegH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldAltRegH.setText("FF");
    fieldAltRegH.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    jLabel22.setText("L:");

    fieldAltRegL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    fieldAltRegL.setText("FF");
    fieldAltRegL.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

    javax.swing.GroupLayout panelAltRegSetLayout = new javax.swing.GroupLayout(panelAltRegSet);
    panelAltRegSet.setLayout(panelAltRegSetLayout);
    panelAltRegSetLayout.setHorizontalGroup(
        panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltRegSetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAltRegSetLayout.createSequentialGroup()
                        .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldAltRegD, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(fieldAltRegE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fieldAltRegH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fieldAltRegL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelAltRegSetLayout.createSequentialGroup()
                        .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fieldAltRegA, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                            .addComponent(fieldAltRegF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelAltRegSetLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldAltRegB, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAltRegSetLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldAltRegC, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
    );

    panelAltRegSetLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, fieldAltRegA, fieldAltRegB, fieldAltRegC, fieldAltRegD, fieldAltRegE, fieldAltRegF);

    panelAltRegSetLayout.setVerticalGroup(
        panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltRegSetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13)
                    .addComponent(fieldAltRegA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(fieldAltRegF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel15)
                    .addComponent(fieldAltRegB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel16)
                    .addComponent(fieldAltRegC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel17)
                    .addComponent(fieldAltRegD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18)
                    .addComponent(fieldAltRegE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel21)
                    .addComponent(fieldAltRegH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAltRegSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel22)
                    .addComponent(fieldAltRegL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Disassembled"));
    jPanel3.setLayout(new java.awt.BorderLayout());

    disasmList.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N
    disasmList.setModel(new javax.swing.AbstractListModel() {
      String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

      @Override
      public int getSize() {
        return strings.length;
      }

      @Override
      public Object getElementAt(int i) {
        return strings[i];
      }
    });
    jPanel3.add(disasmList, java.awt.BorderLayout.CENTER);

    buttonMemory.setText("Memory");
    buttonMemory.addActionListener(this::buttonMemoryActionPerformed);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelFlags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCommonRegisters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(panelRegSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelAltRegSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(buttonMemory, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(5, 5, 5))
    );

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, panelAltRegSet, panelRegSet);

    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelFlags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelRegSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelAltRegSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelCommonRegisters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonMemory)))
                .addGap(13, 13, 13))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void checkBoxZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxZActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_checkBoxZActionPerformed

  private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    this.mainForm.onTracerDeactivated(this);
  }//GEN-LAST:event_formWindowClosed

  private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
    this.mainForm.onTracerActivated(this);
  }//GEN-LAST:event_formWindowActivated
  // End of variables declaration//GEN-END:variables

  private void buttonMemoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMemoryActionPerformed
    new MemoryDialog(this, true, this.module).setVisible(true);
  }//GEN-LAST:event_buttonMemoryActionPerformed

  public int getModuleIndex() {
    return this.moduleIndex;
  }

  public void refreshViewState() {
    final int pc = this.module.getCPU().getPC();

    if (this.module.isActiveRegistersAsMemorySource()) {
      final DefaultListModel model = new DefaultListModel();
      model.addElement("Registers as data source!");
      this.disasmList.setModel(model);
    } else {
      final List<Z80Instruction> instructions = Z80Disasm.decodeList(this, null, pc, 28);

      final DefaultListModel model = new DefaultListModel();

      int address = pc;

      for (final Z80Instruction i : instructions) {
        model.addElement(makeInstructionLine(i, address));
        address += i == null ? 1 : i.getLength();
      }

      this.disasmList.setModel(model);
      this.disasmList.setSelectedIndex(0);

    }
    refreshRegisterValue();
  }

  private void setEnableForComponentsOfPanel(final JPanel panel, final boolean flag) {
    for (final Component c : panel.getComponents()) {
      if (c instanceof AbstractHexValueField) {
        ((AbstractHexValueField) c).setEditable(flag);
      } else if (c instanceof JCheckBox) {
        c.setEnabled(flag);
      }
    }
  }

  private void refreshRegisterValue() {
    if (this.changeEnabled) {
      this.changeEnabled = false;
      setEnableForComponentsOfPanel(this.panelAltRegSet, this.changeEnabled);
      setEnableForComponentsOfPanel(this.panelCommonRegisters, this.changeEnabled);
      setEnableForComponentsOfPanel(this.panelFlags, this.changeEnabled);
      setEnableForComponentsOfPanel(this.panelRegSet, this.changeEnabled);
    }

    final Z80 cpu = this.module.getCPU();
    this.fieldPC.setValue(cpu.getPC());
    this.fieldSP.setValue(cpu.getSP());
    this.fieldIX.setValue(cpu.getRegister(Z80.REG_IX));
    this.fieldIY.setValue(cpu.getRegister(Z80.REG_IY));
    this.fieldIM.setValue(cpu.getRegister(Z80.REG_I));
    this.fieldR.setValue(cpu.getRegister(Z80.REG_R));

    final int regf = cpu.getRegister(Z80.REG_F);

    this.checkBoxC.setSelected((regf & Z80.FLAG_C) != 0);
    this.checkBoxF3.setSelected((regf & Z80.FLAG_X) != 0);
    this.checkBoxF5.setSelected((regf & Z80.FLAG_Y) != 0);
    this.checkBoxH.setSelected((regf & Z80.FLAG_H) != 0);
    this.checkBoxN.setSelected((regf & Z80.FLAG_N) != 0);
    this.checkBoxPV.setSelected((regf & Z80.FLAG_PV) != 0);
    this.checkBoxS.setSelected((regf & Z80.FLAG_S) != 0);
    this.checkBoxZ.setSelected((regf & Z80.FLAG_Z) != 0);

    this.checkBoxIFF1.setSelected(cpu.isIFF1());
    this.checkBoxIFF2.setSelected(cpu.isIFF2());

    this.fieldRegA.setValue(cpu.getRegister(Z80.REG_A));
    this.fieldRegF.setValue(cpu.getRegister(Z80.REG_F));
    this.fieldRegB.setValue(cpu.getRegister(Z80.REG_B));
    this.fieldRegC.setValue(cpu.getRegister(Z80.REG_C));
    this.fieldRegD.setValue(cpu.getRegister(Z80.REG_D));
    this.fieldRegE.setValue(cpu.getRegister(Z80.REG_E));
    this.fieldRegH.setValue(cpu.getRegister(Z80.REG_H));
    this.fieldRegL.setValue(cpu.getRegister(Z80.REG_L));

    this.fieldAltRegA.setValue(cpu.getRegister(Z80.REG_A, true));
    this.fieldAltRegF.setValue(cpu.getRegister(Z80.REG_F, true));
    this.fieldAltRegB.setValue(cpu.getRegister(Z80.REG_B, true));
    this.fieldAltRegC.setValue(cpu.getRegister(Z80.REG_C, true));
    this.fieldAltRegD.setValue(cpu.getRegister(Z80.REG_D, true));
    this.fieldAltRegE.setValue(cpu.getRegister(Z80.REG_E, true));
    this.fieldAltRegH.setValue(cpu.getRegister(Z80.REG_H, true));
    this.fieldAltRegL.setValue(cpu.getRegister(Z80.REG_L, true));

  }

  private String makeInstructionLine(final Z80Instruction instruction, final int address) {
    this.buffer.setLength(0);

    int2hex4(this.buffer, address);
    this.buffer.append("    ");

    if (instruction == null) {
      this.buffer.append("---");
    } else {
      this.buffer.append(instruction.decode(this, address, address));
    }

    return this.buffer.toString();
  }

  @Override
  public byte readAddress(final int address) {
    return this.module.readMemory(this.module.getCPU(), address & 0xFFFF, false);
  }
}
