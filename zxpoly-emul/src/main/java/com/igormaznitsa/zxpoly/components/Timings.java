package com.igormaznitsa.zxpoly.components;

public interface Timings {
  int CPU_FREQ_HZ = 3546900;

  int LINES_PAPER = 192;
  int SCREEN_HIDDEN_LINES = 8;
  int LINES_BEFORE_FIRST_PAPER_LINE = 63;
  int LINES_AFTER_PAPER = 56;
  int TOTAL_LINES = LINES_BEFORE_FIRST_PAPER_LINE + LINES_PAPER + LINES_AFTER_PAPER;
  int LINES_VISIBLE_TOP_BORDER = LINES_BEFORE_FIRST_PAPER_LINE - SCREEN_HIDDEN_LINES;
  int FIRST_LINE_AFTER_PAPER = LINES_BEFORE_FIRST_PAPER_LINE + LINES_PAPER;

  int TSTATES_HIDDEN_LINE = 40;
  int TSTATES_LEFT_LINE_BORDER = 30;
  int TSTATES_RASTER_LINE_DATA = 128;
  int TSTATES_RIGHT_LINE_BORDER = 30;

  int TSTATES_BEFORE_RASTER_LINE_AREA = TSTATES_HIDDEN_LINE + TSTATES_LEFT_LINE_BORDER;
  int TSTATES_AFTER_RASTER_LINE_AREA = TSTATES_BEFORE_RASTER_LINE_AREA + TSTATES_RASTER_LINE_DATA;
  int TSTATES_PER_WHOLE_RASTER_LINE = TSTATES_AFTER_RASTER_LINE_AREA + TSTATES_RIGHT_LINE_BORDER;

  int TOTAL_SCREEN_LINES = LINES_BEFORE_FIRST_PAPER_LINE + LINES_AFTER_PAPER + LINES_PAPER;
  int TOTAL_VISIBLE_SCREEN_LINES = TOTAL_SCREEN_LINES - SCREEN_HIDDEN_LINES;

  int TSTATES_SCREEN_START = SCREEN_HIDDEN_LINES * TSTATES_PER_WHOLE_RASTER_LINE;
  int TSTATES_RASTER_START = LINES_BEFORE_FIRST_PAPER_LINE * TSTATES_PER_WHOLE_RASTER_LINE;
  int TSTATES_RASTER_END = TSTATES_RASTER_START + LINES_PAPER * TSTATES_PER_WHOLE_RASTER_LINE;
  int TSTATES_SCREEN_END = TOTAL_LINES * TSTATES_PER_WHOLE_RASTER_LINE;

  int TSTATES_PER_FRAME = TSTATES_SCREEN_END;

  int TSTATES_INT_NMI_LENGTH = 30;

}
