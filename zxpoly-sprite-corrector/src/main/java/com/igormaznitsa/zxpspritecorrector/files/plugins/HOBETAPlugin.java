/*
 * Copyright (C) 2019 Igor Maznitsa
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

package com.igormaznitsa.zxpspritecorrector.files.plugins;

import com.igormaznitsa.jbbp.JBBPParser;
import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.io.JBBPByteOrder;
import com.igormaznitsa.jbbp.io.JBBPOut;
import com.igormaznitsa.jbbp.mapper.Bin;
import com.igormaznitsa.jbbp.mapper.BinType;
import com.igormaznitsa.jbbp.utils.JBBPUtils;
import com.igormaznitsa.zxpspritecorrector.components.ZXPolyData;
import com.igormaznitsa.zxpspritecorrector.files.FileNameDialog;
import com.igormaznitsa.zxpspritecorrector.files.Info;
import com.igormaznitsa.zxpspritecorrector.files.SessionData;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class HOBETAPlugin extends AbstractFilePlugin {

  public static final JBBPParser HOBETA_FILE_PARSER = JBBPParser.prepare("byte [8] name; byte type; <ushort start; <ushort length; skip; ubyte sectors; <ushort checksum; byte [_] data;");

  public HOBETAPlugin() {
    super();
  }

  @Override
  public String getPluginUID() {
    return "HBTA";
  }

  @Override
  public String getToolTip(final boolean forExport) {
    return "A Hobeta file format";
  }

  @Override
  public boolean doesImportContainInsideFileList() {
    return false;
  }

  @Override
  public List<Info> getImportingContainerFileList(final File file) {
    return null;
  }

  @Override
  public String getImportingFileInfo(final File file) {
    try {
      JBBPBitInputStream in = null;
      try {
        final StringBuilder result = new StringBuilder();

        final Hobeta hobeta = HOBETA_FILE_PARSER.parse(new FileInputStream(file)).mapTo(new Hobeta());
        result.append("     Name:").append(hobeta.name).append("  ").append('\n');
        result.append("     Type:").append((char) hobeta.type).append("  ").append('\n');
        result.append("    Start:").append(hobeta.start).append("  ").append('\n');
        result.append("   Length:").append(hobeta.length).append(" bytes").append("  ").append('\n');
        result.append("  Sectors:").append(hobeta.sectors).append(" sectors").append("  ");

        return result.toString();
      } finally {
        JBBPUtils.closeQuietly(in);
      }
    } catch (Exception ex) {
      return null;
    }
  }

  @Override
  public ReadResult readFrom(final File file, final int index) throws IOException {
    final byte[] wholeFile = FileUtils.readFileToByteArray(file);
    final Hobeta parsed = HOBETA_FILE_PARSER.parse(wholeFile).mapTo(new Hobeta());
    return new ReadResult(new ZXPolyData(new Info(parsed.name, (char) (parsed.type & 0xFF), parsed.start, parsed.length, 0), this, parsed.data), null);
  }

  @Override
  public String getPluginDescription(final boolean forExport) {
    return "Hobeta file";
  }

  @Override
  public void writeTo(final File file, final ZXPolyData data, final SessionData session) throws IOException {
    final File dir = file.getParentFile();
    final char zxType = data.getInfo().getType();
    String name = file.getName();
    if (FilenameUtils.getExtension(name).isEmpty()) {
      name = name + ".$" + zxType;
    }

    final String zxName = data.getInfo().getName();

    final FileNameDialog nameDialog = new FileNameDialog(
        this.mainFrame,
        "Base file name is " + file.getName(),
        new String[] {addNumberToFileName(name, 0), addNumberToFileName(name, 1), addNumberToFileName(name, 2), addNumberToFileName(name, 3)},
        new String[] {prepareNameForTRD(zxName, 0), prepareNameForTRD(zxName, 1), prepareNameForTRD(zxName, 2), prepareNameForTRD(zxName, 3)},
        new char[] {zxType, zxType, zxType, zxType}
    );
    nameDialog.setVisible(true);

    if (nameDialog.approved()) {
      final String[] fileNames = nameDialog.getFileName();
      final String[] zxNames = nameDialog.getZxName();
      final Character[] types = nameDialog.getZxType();

      for (int i = 0; i < 4; i++) {
        final File savingfile = new File(dir, fileNames[i]);
        if (!writeDataBlockAsHobeta(savingfile, zxNames[i], (byte) types[i].charValue(), data.getInfo().getStartAddress(), data.getDataForCPU(i))) {
          break;
        }
      }
    }
  }

  @Override
  public javax.swing.filechooser.FileFilter getImportFileFilter() {
    return this;
  }

  @Override
  public javax.swing.filechooser.FileFilter getExportFileFilter() {
    return null;
  }

  @Override
  public String getExtension(final boolean forExport) {
    return null;
  }

  private int makeCRC(final byte[] array) {
    int crc = 0;
    for (int i = 0; i < array.length; crc = crc + (array[i] * 257) + i, i++) {
    }
    return crc;
  }

  private boolean writeDataBlockAsHobeta(final File file, final String name, final byte type, final int start, final byte[] data) throws IOException {
    final byte[] header = JBBPOut.BeginBin().ByteOrder(JBBPByteOrder.LITTLE_ENDIAN).Byte(name).Byte(type).Short(start, data.length).Byte((data.length >>> 8) + 1, 0).End().toByteArray();
    final byte[] full = JBBPOut.BeginBin().ByteOrder(JBBPByteOrder.LITTLE_ENDIAN).Byte(header).Short(makeCRC(header)).Byte(data).End().toByteArray();
    return saveDataToFile(file, full);
  }

  @Override
  public boolean accept(final File pathname) {
    return pathname != null && (pathname.isDirectory() || pathname.getName().lastIndexOf(".$") == (pathname.getName().length() - 3));
  }

  @Override
  public String getDescription() {
    return getToolTip(false) + " (*.$?)";
  }

  private static final class Hobeta {

    @Bin(type = BinType.BYTE_ARRAY)
    String name;
    @Bin(type = BinType.BYTE)
    byte type;
    @Bin(type = BinType.USHORT)
    int start;
    @Bin(type = BinType.USHORT)
    int length;
    @Bin(type = BinType.UBYTE)
    int sectors;
    @Bin(type = BinType.USHORT)
    int checksum;
    @Bin
    byte[] data;
  }

}
