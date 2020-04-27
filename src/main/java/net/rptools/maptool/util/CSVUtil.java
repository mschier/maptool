/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.util;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
  /** returns a row of values as a list returns null if you are past the end of the line */
  public static List<String> parseLine(Reader r) throws IOException {
    int ch = r.read();
    while (ch == '\r') {
      // ignore linefeed characters wherever they are, particularly just before end of file
      ch = r.read();
    }
    if (ch < 0) {
      return null;
    }
    ArrayList<String> store = new ArrayList<>();
    StringBuilder curVal = new StringBuilder();
    boolean inquotes = false;
    boolean started = false;
    while (ch >= 0) {
      if (inquotes) {
        started = true;
        if (ch == '\"') {
          inquotes = false;
        } else {
          curVal.append((char) ch);
        }
      } else {
        if (ch == '\"') {
          inquotes = true;
          if (started) {
            // if this is the second quote in a value, add a quote
            // this is for the double quote in the middle of a value
            curVal.append('\"');
          }
        } else if (ch == ',') {
          store.add(curVal.toString());
          curVal = new StringBuilder();
          started = false;
        } else if (ch == '\r') {
          // ignore LF characters
        } else if (ch == '\n') {
          // end of a line, break out
          break;
        } else {
          curVal.append((char) ch);
        }
      }
      ch = r.read();
    }
    store.add(curVal.toString());
    return store;
  }
}
