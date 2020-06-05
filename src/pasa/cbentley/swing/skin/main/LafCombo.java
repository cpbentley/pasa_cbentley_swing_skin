/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.swing.skin.main;
public class LafCombo {

   private String name;

   private String path;

   public LafCombo(String path, String name) {
      this.setPath(path);
      this.setName(name);
   }

   public String getName() {
      return name;
   }

   public String getPath() {
      return path;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPath(String path) {
      this.path = path;
   }
}