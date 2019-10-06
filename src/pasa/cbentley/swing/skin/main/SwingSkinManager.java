/*
 * (c) 2018-2019 Charles-Philip Bentley
 * This code is licensed under CC by-nc-nd 4.0 (see LICENSE.txt for details)
 * Contact author for uses outside of the NonCommercial-NoDerivatives clauses.   
 */
package pasa.cbentley.swing.skin.main;

import static java.awt.event.KeyEvent.VK_R;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.jtattoo.plaf.AbstractLookAndFeel;
import com.jtattoo.plaf.AbstractTheme;
import com.jtattoo.plaf.JTattooUtilities;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.IPrefs;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.logging.ITechLvl;
import pasa.cbentley.swing.ctx.SwingCtx;
import pasa.cbentley.swing.menu.ITechMenu;
import pasa.cbentley.swing.skin.ctx.ITechPrefsSwingSkin;
import pasa.cbentley.swing.skin.ctx.ITechStringsSwingSkin;
import pasa.cbentley.swing.skin.ctx.SwingSkinCtx;
import pasa.cbentley.swing.widgets.b.BMenu;
import pasa.cbentley.swing.widgets.b.BMenuItem;

/**
 * Integrates Themes and JTattoo into a {@link JFrame} based application and its {@link JMenuBar}.
 * <br>
 * <p>
 * <b>Features</b>:
 * <li>Add Look and Feel Items to the {@link JMenu}.
 * <li>On demand loading when the user selects the Look and Feel Menu.
 * <li>Menu localization
 * <li>Favorite list: User can add or remove current look and feel in favorites. The list is saved in {@link Preferences}.
 * <li>Simple visual test method with {@link SwingSkinManager#main(String[])}
 * <br>
 * </p>
 * <br>
 * <p>
 * <b>How to use</b>:
 * <ol>
 * <li> Create a new instance with a {@link Preferences}
 * <li> Create a {@link JMenuBar}. <code> JMenuBar mb = new JMenuBar(); </code>. Add the menu bar to your {@link JFrame}
 * <li> Add the {@link SwingSkinManager#getRootMenu()} to the {@link JMenuBar}
 * <li> That's it!
 * </ol>
 * </p>
 * <p>
 * <b>String Localization</b>
 * <br>
 * By default, English Strings are hardcoded by default.<br>
 * A real application will call the {@link SwingSkinManager#guiUpdate(ResourceBundle)}.
 * <br>
 * <br>
 * The {@link ResourceBundle} must have the following keys
 * <li> {@link SwingSkinManager#BUNDLE_KEY_FAV}
 * <li> {@link SwingSkinManager#BUNDLE_KEY_FAV_ADD}
 * <li> {@link SwingSkinManager#BUNDLE_KEY_FAV_REMOVE}
 * <li> {@link SwingSkinManager#BUNDLE_KEY_MAIN_MENU}
 * <li> {@link SwingSkinManager#BUNDLE_KEY_OTHERS}
 * <li> {@link SwingSkinManager#BUNDLE_KEY_SYSTEM}
 * </p>
 * @author Charles Bentley
 * @version 1.0
 *
 */
public class SwingSkinManager implements ActionListener, MenuListener, IStringable, ITechStringsSwingSkin, ITechPrefsSwingSkin, ITechMenu {

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

   /**
    * Current active Laf as an {@link Action}.
    * <br>
    * Will be initialized by {@link SwingSkinManager#populateLookAndFeelMenu(JMenu)}
    */
   private LafAction            currentAction;

   /**
    * Current action from the favorites. Null if current theme is not a favorite
    */
   private LafAction            currentActionFav;

   private String               currentLaf;

   private Icon                 iconSelection;

   /**
    * Will keep the first look and feel from start.
    */
   private String               initLookClassName;

   private String               initThemeName;

   private BMenuItem            jmiFavAdd;

   private BMenuItem            jmiFavRemove;

   /**
    * 
    */
   private ButtonGroup          lafButtonGroup;

   private ButtonGroup          lafButtonGroupFav;

   private ArrayList<LafCombo>  listTatoos;

   private BMenu                menuFav;

   private BMenu                menuOthers;

   private BMenu                menuRoot;

   private BMenu                menuSystem;

   /**
    * Collection of all LAF actions
    */
   private ArrayList<LafAction> myLafActions = new ArrayList<>();

   private boolean              isUsingDefaultKeyShortcuts;

   protected final SwingSkinCtx ssc;

   protected final SwingCtx     sc;

   private BMenuItem            itemFontIncrease;

   private BMenuItem            itemFontDecrease;

   private BMenuItem            itemRandom;

   /**
    * Upon creation, module looks for installed/accessible look and feels.
    * 
    */
   public SwingSkinManager(SwingSkinCtx ssc) {
      this.ssc = ssc;
      this.sc = ssc.getSwingCtx();
      //the very first thing we need to do is read the preference and set the look and feel
      prefsInit();

      //the initialization of menu must be done after the lookandfeel preference has been loaded
      //otherwise the first look for those menu will be metal
      menuRoot = new BMenu(sc, sMainMenu);
      menuRoot.setMnemonic(KeyEvent.VK_K);
      menuRoot.addMenuListener(this); //if menu activated with the keyboard
      //when using the most common case.. use a thread to smooth out user experience
      menuRoot.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            //do this check in a SwingWorker
            checkPopulateRootMenu();
         }
      });

   }

   private void initMenuMain() {
      //first build the menu for the system look and feels
      menuSystem = new BMenu(sc, sSystemLnF);
      menuOthers = new BMenu(sc, sOthers);
      menuFav = new BMenu(sc, sFavorite);

      jmiFavRemove = new BMenuItem(sc, this, sRemovefavorite);

      jmiFavAdd = new BMenuItem(sc, this, sAddfavorite);

      itemFontIncrease = new BMenuItem(sc, this, sFontIncrease);
      itemFontDecrease = new BMenuItem(sc, this, sFontDecrease);
      itemRandom = new BMenuItem(sc, this, sRandom);

      if (isUsingDefaultKeyShortcuts) {
         itemFontIncrease.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, modAlt));
         itemFontDecrease.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, modAlt));
         itemRandom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, modAlt));
      }

      menuFav.add(jmiFavAdd);
      menuFav.add(jmiFavRemove);
      menuFav.addSeparator();

   }

   /**
    * The LookAndFeel changes are processed by {@link LafAction#actionPerformed(ActionEvent)}
    * <br>
    */
   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == jmiFavAdd) {
         cmdFavoriteAdd();
      } else if (src == jmiFavRemove) {
         cmdFavoriteRemove();
      } else if (src == itemFontDecrease) {
         cmdFontSizeDecrease();
      } else if (src == itemFontIncrease) {
         cmdFontSizeIncrease();
      } else if (src == itemRandom) {
         cmdRandomSet();
      }
   }

   public void cmdFavoriteRemove() {
      JMenuItem item = getActionFavMatch(currentAction);
      if (item != null) {
         lafButtonGroupFav.remove(item);
         lafButtonGroupFav.clearSelection();
         menuFav.remove(item);
         menuFav.setIcon(null);
         menuFav.repaint();
         //update the favorite string

         prefsSave();
      }
   }

   public void cmdFavoriteAdd() {
      //get current
      if (currentAction != null) {
         //check if already in the list
         JMenuItem item = getActionFavMatch(currentAction);
         if (item == null) {
            //create a new lafaction with menufav as root menu
            LookAndFeelInfo laf = currentAction.getInfo();
            AbstractLookAndFeel alf = currentAction.getAbstractLookAndFeel();
            String theme = currentAction.getTheme();
            LafAction lafAction = new LafAction(ssc, this, laf, theme, alf, menuFav);
            //not in the list
            JRadioButtonMenuItem rbm = new JRadioButtonMenuItem(lafAction);
            lafButtonGroupFav.add(rbm);
            menuFav.add(rbm);
            menuFav.setIcon(iconSelection);
            //refresh
            lafButtonGroupFav.clearSelection();
            rbm.setSelected(true);
            menuFav.repaint();
         }

         //save on the spot
         prefsSave();
      }
   }

   /**
    * Create the {@link JRadioButtonMenuItem} for the LookAndFeel action.
    * <br>
    * @param lookAndFeelMenu
    * @param laf
    * @param lafClassName 
    */
   private void addRadioButton(UIManager.LookAndFeelInfo laf, String lafClassName) {
      String name = laf.getName();
      LafAction action = getAction(name, null);
      //remove this check in production
      if (action == null)
         throw new RuntimeException("Could not find Action for " + name);

      myLafActions.add(action);
      JRadioButtonMenuItem buttonLaf = new JRadioButtonMenuItem(action);
      if (lafClassName.equals(initLookClassName) && initThemeName == null) {
         action.getActionRootMenu().setIcon(iconSelection);
         buttonLaf.setSelected(true);
         currentAction = action;
      }
      lafButtonGroup.add(buttonLaf);
      action.getMenu().add(buttonLaf);
   }

   private void addSeparator(JMenu menu, String lafClassName, String lastLafClassName) {
      //insert the separator before adding the new laf
      int indexPoint = lafClassName.indexOf('.');
      if (indexPoint != -1) {
         indexPoint = lafClassName.indexOf('.', indexPoint + 1);
         if (indexPoint != -1) {
            String startOfNewLaf = lafClassName.substring(0, indexPoint);
            //System.out.println("lafClassName=" + lafClassName + " lastLafClassName=" + lastLafClassName + " startOfNewLaf=" + startOfNewLaf);
            if (lastLafClassName != null && !lastLafClassName.startsWith(startOfNewLaf)) {
               //add a separator for changing families of LAF
               menu.addSeparator();
            }
         }
      }
   }

   private void checkPopulateRootMenu() {
      if (menuRoot.getItemCount() == 0) {
         populateLookAndFeelMenu(menuRoot);
         sc.guiUpdateOnChildrenMenu(menuRoot);
      }
   }

   public void cmdRandomSet() {
      if (currentAction != null) {
         checkPopulateRootMenu(); //make sure actions are loaded
         int rIndex = ssc.getUCtx().getRandom().nextInt(myLafActions.size());
         LafAction action = myLafActions.get(rIndex);
         executeSetMyLafTheme(action);
         syncReal(action); //sync the ui for new action
      }
   }

   public boolean cmdFontSizeDecrease() {
      return cmdFontSizeZipOver(1);
   }

   /**
    * Separators 
    * @param type
    * @return
    */
   private boolean cmdFontSizeZipOver(int type) {
      checkPopulateRootMenu();
      JMenu m = currentAction.getMenu();
      Component[] menuComponents = m.getMenuComponents();
      for (int i = 0; i < menuComponents.length; i++) {
         if (menuComponents[i] instanceof JRadioButtonMenuItem) {
            JRadioButtonMenuItem radio = (JRadioButtonMenuItem) menuComponents[i];
            if (radio.getAction() == currentAction) {
               //we found it.. increase by going up IF not a separator
               if (type == 0) {
                  //increases
                  if (i + 1 < menuComponents.length) {
                     //might a Separator which will return false and stop
                     Component componentUp = menuComponents[i + 1];
                     return setLafActionIfMenuItem(componentUp);
                  }
               } else {
                  //increases
                  if (i - 1 >= 0) {
                     Component componentDown = menuComponents[i - 1];
                     return setLafActionIfMenuItem(componentDown);
                  }
               }
            }
         }
      }
      return false;
   }

   /**
    * Try to increase Font size of look and feel if possible
    */
   public boolean cmdFontSizeIncrease() {
      return cmdFontSizeZipOver(0);
   }

   /**
    * 
    * @param lafsInstalled
    */
   private void createActions(UIManager.LookAndFeelInfo[] lafsInstalled) {
      for (int i = 0; i < lafsInstalled.length; i++) {
         UIManager.LookAndFeelInfo laf = lafsInstalled[i];
         String lafClassName = laf.getClassName();
         if (lafClassName.startsWith("com.jtattoo.plaf")) {
            try {
               Class c = Class.forName(lafClassName);
               //create new instance
               AbstractLookAndFeel alf = (AbstractLookAndFeel) c.newInstance();
               List listThemes = alf.getMyThemes();
               JMenu menu = new JMenu(laf.getName());
               for (Iterator iterator = listThemes.iterator(); iterator.hasNext();) {
                  String theme = (String) iterator.next();
                  LafAction action = new LafAction(ssc, this, laf, theme, alf, menu);
                  myLafActions.add(action);
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         } else {
            JMenu menu = null;
            if (lafClassName.startsWith("javax.swing.plaf") || lafClassName.startsWith("com.sun.java")) {
               menu = menuSystem;
            } else {
               menu = menuOthers;
            }
            LafAction action = new LafAction(ssc, this, laf, menu);
            myLafActions.add(action);
         }
      }

   }

   public void executeSetMyLafTheme(LafAction action) {
      AbstractLookAndFeel alf = action.getAbstractLookAndFeel();
      if (action.getTheme() != null && alf != null) {
         alf.setMyTheme(action.getTheme());
      }
      setApplicationLookAndFeel(action.getInfo().getClassName());
      JMenu newMenuSelected = action.getMenu();
      //clear menu of current action
      if (currentAction != null) {
         currentAction.getActionRootMenu().setIcon(null);
      }
      if (currentActionFav != null) {
         currentActionFav.getActionRootMenu().setIcon(null);
         //also deselects the radio button

      }
      //there might be several
      if (newMenuSelected != null) {
         newMenuSelected.setIcon(iconSelection);
      }
      //iterate over all menu and set the right icons.
      if (newMenuSelected == menuFav) {
         syncReal(action); //
         currentActionFav = action;
      } else {
         //we must clear favs so that when go from fav to regular, selection deselects. setSelected(false) does not work bug?
         lafButtonGroupFav.clearSelection();
         syncFav(action);
         currentAction = action;
      }
   }

   private LafAction getAction(String lafName, String theme) {
      for (Iterator iterator = myLafActions.iterator(); iterator.hasNext();) {
         LafAction lafAction = (LafAction) iterator.next();
         if (lafAction.isMatch(lafName, theme)) {
            return lafAction;
         }
      }
      return null;
   }

   private JMenuItem getActionFavMatch(LafAction ac) {
      int num = menuFav.getItemCount();
      for (int i = 0; i < num; i++) {
         JMenuItem ji = menuFav.getItem(i);
         if (ji instanceof JRadioButtonMenuItem) {
            LafAction action = (LafAction) ji.getAction();
            if (ac.isMatch(action)) {
               return ji;
            }
         }
      }
      return null;
   }

   /**
    * Returns the current laf
    * @return
    */
   public String getCurrentLaf() {
      return currentLaf;
   }

   public List<LafCombo> getLAFTatoos() {
      if (listTatoos == null) {
         listTatoos = new ArrayList<LafCombo>();
         listTatoos.add(new LafCombo("com.jtattoo.plaf.acryl.AcrylLookAndFeel", "Acryl"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.aero.AeroLookAndFeel", "Aero"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel", "Aluminium"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel", "Bernstein"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.fast.FastLookAndFeel", "Fast"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.graphite.GraphiteLookAndFeel", "Graphite"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.hifi.HiFiLookAndFeel", "HiFi"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.luna.LunaLookAndFeel", "Luna"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.mcwin.McWinLookAndFeel", "McWin"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.mint.MintLookAndFeel", "Mint"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.noire.NoireLookAndFeel", "Noire"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.smart.SmartLookAndFeel", "Smart"));
         listTatoos.add(new LafCombo("com.jtattoo.plaf.texture.TextureLookAndFeel", "Texture"));
      }
      return listTatoos;
   }

   /**
    * Returns the {@link JMenu} to be added to the {@link JMenuBar}.
    * <br>
    * There is nothing to do. The {@link MenuListener} is already working.
    * <br>
    * However only one instance of a Menu can be added to a {@link JMenuBar}.
    * If you want to have 2 {@link JMenuBar} each with a Skin Menu
    * @return
    */
   public JMenu getRootMenu() {
      return menuRoot;
   }

   /**
    * Put the LnF that work well here
    */
   private void installSome() {
      //jtattoos
      installTry("com.jtattoo.plaf.acryl.AcrylLookAndFeel", "Acryl");
      installTry("com.jtattoo.plaf.aero.AeroLookAndFeel", "Aero");
      installTry("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel", "Aluminium");
      installTry("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel", "Bernstein");
      installTry("com.jtattoo.plaf.fast.FastLookAndFeel", "Fast");
      installTry("com.jtattoo.plaf.graphite.GraphiteLookAndFeel", "Graphite");
      installTry("com.jtattoo.plaf.hifi.HiFiLookAndFeel", "HiFi");
      installTry("com.jtattoo.plaf.luna.LunaLookAndFeel", "Luna");
      installTry("com.jtattoo.plaf.mcwin.McWinLookAndFeel", "McWin");
      installTry("com.jtattoo.plaf.mint.MintLookAndFeel", "Mint");
      installTry("com.jtattoo.plaf.noire.NoireLookAndFeel", "Noire");
      installTry("com.jtattoo.plaf.smart.SmartLookAndFeel", "Smart");
      installTry("com.jtattoo.plaf.texture.TextureLookAndFeel", "Texture");
   }

   private void installTry(String classPath, String title) {
      try {
         Class.forName(classPath);
         UIManager.installLookAndFeel(title, classPath);
      } catch (ClassNotFoundException e) {
      }
   }

   /**
    * Called last when lazily populating the Skin root menu
    * <br>
    * Read favorites from preference string
    */
   private void loadFavorites() {
      String favoriteString = ssc.getUIPref().get(PREF_LOOKANDFEEL_FAV, "");
      if (favoriteString != null && !favoriteString.equals("")) {
         String[] str = favoriteString.split(";");
         if (str != null) {
            for (int i = 0; i < str.length; i++) {
               String lookTheme = str[i];
               LafAction actionFav = null;
               int indexDot = lookTheme.indexOf('.');
               if (indexDot == -1) {
                  LafAction lafAction = getAction(lookTheme, null);
                  //look up the existing
                  if (lafAction != null) {
                     actionFav = new LafAction(ssc, this, lafAction.getInfo(), menuFav);
                  }
               } else {
                  //split theme
                  String laf = lookTheme.substring(0, indexDot);
                  String theme = lookTheme.substring(indexDot + 1, lookTheme.length());
                  LafAction actionRegular = getAction(laf, theme);
                  //look up the existing
                  if (actionRegular != null) {
                     actionFav = new LafAction(ssc, this, actionRegular.getInfo(), theme, actionRegular.getAbstractLookAndFeel(), menuFav);
                  }
                  //check if init theme and the set cu
                  String className = actionRegular.getInfo().getClassName();
                  if (className.equals(initLookClassName) && theme.equals(initThemeName)) {
                     currentActionFav = actionFav;
                     currentActionFav.getActionRootMenu().setIcon(iconSelection);
                  }
               }
               JRadioButtonMenuItem rad = new JRadioButtonMenuItem(actionFav);
               if (currentActionFav == actionFav) {
                  rad.setSelected(true);
               }
               lafButtonGroupFav.add(rad);
               menuFav.add(rad);
               //check if already
            }
         }
      }
   }

   public void menuCanceled(MenuEvent e) {
   }

   public void menuDeselected(MenuEvent e) {
   }

   public void menuSelected(MenuEvent e) {
      if (e.getSource() == menuRoot) {
         checkPopulateRootMenu();
      }
   }

   /**
    * A {@link JMenuBar} will list a JMenu and call this method only when the user selects
    * the menu. Use {@link MenuListener#menuSelected(MenuEvent)}
    * @param lookAndFeelMenu
    */
   private void populateLookAndFeelMenu(JMenu lookAndFeelMenu) {

      initMenuMain();
      //list favorite here
      installSome();

      UIManager.LookAndFeelInfo[] lafsInstalled = UIManager.getInstalledLookAndFeels();

      //we create LafActions for all installed look and feels and their themes
      createActions(lafsInstalled);

      //add the favorite menu
      lookAndFeelMenu.add(menuFav);
      lookAndFeelMenu.add(menuSystem);
      lookAndFeelMenu.addSeparator();
      lookAndFeelMenu.add(itemRandom);
      lookAndFeelMenu.addSeparator();
      lookAndFeelMenu.add(itemFontIncrease);
      lookAndFeelMenu.add(itemFontDecrease);

      lafButtonGroup = new ButtonGroup(); //only 1 LAF button can be selected at a time
      lafButtonGroupFav = new ButtonGroup();

      String lastLafClassName = null;
      for (int i = 0; i < lafsInstalled.length; i++) {
         UIManager.LookAndFeelInfo laf = lafsInstalled[i];
         String lafClassName = laf.getClassName();
         String lafName = laf.getName();

         addSeparator(lookAndFeelMenu, lafClassName, lastLafClassName);
         //System.out.println("LAF Class=" + lafClassName + " = " + lafName);

         //load jtatto themes
         if (lafClassName.startsWith("com.jtattoo.plaf")) {
            try {
               Class c = Class.forName(lafClassName);
               AbstractLookAndFeel alf = (AbstractLookAndFeel) c.newInstance();
               List listThemes = alf.getMyThemes();
               JMenu menu = null;
               int count = 0;
               //all jtattoo themes have several themes
               for (Iterator iterator = listThemes.iterator(); iterator.hasNext();) {
                  String theme = (String) iterator.next();
                  JRadioButtonMenuItem buttonTheme = new JRadioButtonMenuItem(theme);
                  lafButtonGroup.add(buttonTheme);

                  //get the action
                  LafAction action = getAction(lafName, theme);
                  menu = action.getActionRootMenu();
                  if (lafClassName.equals(initLookClassName) && theme.equals(initThemeName)) {
                     menu.setIcon(iconSelection);
                     buttonTheme.setSelected(true);
                     currentAction = action;
                  }
                  menu.add(buttonTheme);
                  buttonTheme.setAction(action);
                  myLafActions.add(action);
                  //add a separator every 4
                  count++;
                  if (count % 4 == 0) {
                     menu.addSeparator();
                  }
               }
               lookAndFeelMenu.add(menu);
            } catch (ClassNotFoundException e) {
               e.printStackTrace();
            } catch (InstantiationException e) {
               e.printStackTrace();
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            }
         } else {
            addRadioButton(laf, lafClassName);
         }

         lastLafClassName = lafClassName;
      }

      if (menuOthers.getItemCount() != 0) {
         lookAndFeelMenu.add(menuOthers);
      }
      loadFavorites();

   }

   private void prefsInit() {
      IPrefs prefs = ssc.getUIPref();
      String lookFeelClassName = prefs.get(PREF_LOOKANDFEEL, "");
      String lookFeelTheme = prefs.get(PREF_LOOKANDFEEL_THEME, "");
      if (!lookFeelClassName.equals("")) {
         try {
            if (lookFeelClassName.startsWith("com.jtattoo.plaf")) {
               if (!lookFeelTheme.equals("")) {
                  Class c = Class.forName(lookFeelClassName);
                  AbstractLookAndFeel alf = (AbstractLookAndFeel) c.newInstance();
                  List listThemes = alf.getMyThemes();
                  for (Iterator iterator = listThemes.iterator(); iterator.hasNext();) {
                     String theme = (String) iterator.next();
                     if (theme.equals(lookFeelTheme)) {
                        initThemeName = lookFeelTheme;
                        sc.getLog().consoleLog("Restoring Theme " + lookFeelTheme);
                        alf.setMyTheme(lookFeelTheme);
                        break;
                     }
                  }
               }
            }
            initLookClassName = lookFeelClassName;
            //#debug
            toDLog().pFlow("Restoring Look and Feel " + lookFeelClassName, this, SwingSkinManager.class, "prefsInit", ITechLvl.LVL_05_FINE, true);
            //System.out.println("Restoring Look and Feel " + lookFeelClassName);
            UIManager.setLookAndFeel(lookFeelClassName);

            //if success here create the current Laf
            LookAndFeel laf = UIManager.getLookAndFeel();
            UIManager.LookAndFeelInfo lafi = new UIManager.LookAndFeelInfo(laf.getName(), lookFeelClassName);
            currentAction = new LafAction(ssc, this, lafi, lookFeelTheme, null, null);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   /**
    * Save LnF current state using {@link Preferences} object loaded at start.
    */
   public void prefsSave() {

      //there is nothing to save if the menu was not opened
      if (menuRoot.getItemCount() == 0) {
         //#debug
         ssc.toDLog().pFlow("Skin menu not actived. No changes to save.", null, SwingSkinManager.class, "prefsSave", ITechLvl.LVL_05_FINE, true);
         return;
      }

      IPrefs prefs = ssc.getUIPref();
      String value = UIManager.getLookAndFeel().getClass().getName();
      LookAndFeel laf = UIManager.getLookAndFeel();
      prefs.put(PREF_LOOKANDFEEL, value);

      if (laf instanceof AbstractLookAndFeel) {
         AbstractLookAndFeel alf = (AbstractLookAndFeel) laf;
         AbstractTheme at = alf.getTheme(); //must be called on instance. ignore IDE warning
         String themeName = at.getInternalName(); //must be called on instance. ignore IDE warning
         //#debug
         toDLog().pFlow("Saving Look and Feel " + value + " Theme=" + themeName, null, SwingSkinManager.class, "prefsSave", ITechLvl.LVL_05_FINE, true);
         //System.out.println("Saving Look and Feel " + value + " " + themeName);
         prefs.put(PREF_LOOKANDFEEL_THEME, themeName);
      } else {
         prefs.put(PREF_LOOKANDFEEL_THEME, "");
      }
      //favorite string before
      String favs = prefs.get(PREF_LOOKANDFEEL_FAV, "");
      if (!favs.equals("")) {
         //#debug
         ssc.toDLog().pFlow("Favorites were " + favs, null, SwingSkinManager.class, "prefsSave", ITechLvl.LVL_05_FINE, true);
      }
      //do the favorites
      int num = menuFav.getItemCount();
      StringBuilder sb = new StringBuilder(100);
      for (int i = 0; i < num; i++) {
         JMenuItem ji = menuFav.getItem(i);
         if (ji instanceof JRadioButtonMenuItem) {
            LafAction action = (LafAction) ji.getAction();
            sb.append(action.getInfo().getName());
            if (action.getTheme() != null) {
               sb.append('.');
               sb.append(action.getTheme());
            }
            sb.append(';');
         }
      }
      //
      String favString = sb.toString();
      if (favString.length() < favs.length()) {
         //#debug
         toDLog().pFlow("Favorites were removed" + favString, this, SwingSkinManager.class, "prefsSave", ITechLvl.LVL_05_FINE, true);

      }
      if (favString.equals("")) {
         //#debug
         toDLog().pFlow("No Favorites to save" + favString, null, SwingSkinManager.class, "prefsSave", ITechLvl.LVL_05_FINE, true);
      } else {
         //#debug
         toDLog().pFlow("Saving Favorites as " + favString, null, SwingSkinManager.class, "prefsSave", ITechLvl.LVL_05_FINE, true);
      }
      prefs.put(PREF_LOOKANDFEEL_FAV, favString);
   }

   private void setApplicationLookAndFeel(String className) {
      if (className != null) {
         try {
            LookAndFeel oldLAF = UIManager.getLookAndFeel();
            boolean oldDecorated = false;
            if (oldLAF instanceof MetalLookAndFeel) {
               oldDecorated = true;
            }
            if (oldLAF instanceof AbstractLookAndFeel) {
               oldDecorated = AbstractLookAndFeel.getTheme().isWindowDecorationOn();
            }
            JFrame.setDefaultLookAndFeelDecorated(false);
            JDialog.setDefaultLookAndFeelDecorated(false);
            UIManager.setLookAndFeel(className);
            currentLaf = className;

            LookAndFeel newLAF = UIManager.getLookAndFeel();
            boolean newDecorated = false;
            if (newLAF instanceof MetalLookAndFeel) {
               newDecorated = true;
            }
            if (newLAF instanceof AbstractLookAndFeel) {
               newDecorated = AbstractLookAndFeel.getTheme().isWindowDecorationOn();
            }
            if (oldDecorated != newDecorated) {
               // Reboot the application
               String message = "Reboot the application " + oldLAF.getName() + " " + oldDecorated + " new " + newLAF.getName() + " " + newDecorated;
               sc.getLog().consoleLog(message);
            }
            updateComponentTree();
         } catch (Exception e) {
            e.printStackTrace();
         }

         updateComponentTree();
      }
   }

   /**
    * Sets the icon that will be displayed next to the selected look and feel menu.
    * <br>
    * @param icon
    */
   public void setIconSelected(Icon icon) {
      this.iconSelection = icon;
   }

   private boolean setLafActionIfMenuItem(Component c) {
      if (c instanceof JRadioButtonMenuItem) {
         JRadioButtonMenuItem item = (JRadioButtonMenuItem) c;
         Action itemAction = item.getAction();
         if (itemAction instanceof LafAction) {
            executeSetMyLafTheme((LafAction) itemAction);
            item.setSelected(true);
            return true;
         }
      }
      return false;
   }

   /**
    * Called when a regular Skin is changed. Look up the favorites
    * and if its inside.. select it in the group
    */
   private void syncFav(LafAction currentAction) {
      int num = menuFav.getItemCount();
      for (int i = 0; i < num; i++) {
         JMenuItem ji = menuFav.getItem(i);
         //a simple reference check is not sufficient since fav skins have their own action.
         if (ji instanceof JRadioButtonMenuItem) {
            LafAction actionFav = (LafAction) ji.getAction();
            if (currentAction.isMatch(actionFav)) {
               ji.setSelected(true);
               currentActionFav = actionFav;
               actionFav.getActionRootMenu().setIcon(iconSelection);
               break;
            }
         }
      }
   }

   /**
    * Called when a fav item has been selected
    */
   private void syncReal(LafAction favAction) {
      int countI = menuRoot.getItemCount();
      for (int i = 0; i < countI; i++) {
         JMenuItem ji = menuRoot.getItem(i);
         if (ji != menuFav) {
            if (ji instanceof JMenu) {
               JMenu m = ((JMenu) ji);
               int countK = m.getItemCount();
               boolean isSelected = false;
               for (int k = 0; k < countK; k++) {
                  JMenuItem jik = m.getItem(k);
                  if (jik != null) {
                     Action ac = jik.getAction();
                     if (ac != null && ac instanceof LafAction) {
                        LafAction actionReg = (LafAction) ac;
                        if (favAction.isMatch(actionReg)) {
                           currentAction = actionReg;
                           jik.setSelected(true);
                           m.setIcon(iconSelection);
                           isSelected = true;
                        }
                     }
                  }
               }
               //remove the others
               if (!isSelected && m.getIcon() != null) {
                  m.setIcon(null);
               }
            }
         }
      }
   }

   public IDLog toDLog() {
      return ssc.toDLog();
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "PascalSkinManager");
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "PascalSkinManager");
   }

   public UCtx toStringGetUCtx() {
      return ssc.getUCtx();
   }
   //#enddebug

   /**
    * 
    */
   private void updateComponentTree() {
      // Update the application
      if (JTattooUtilities.getJavaVersion() >= 1.6) {
         Window windows[] = Window.getWindows();
         for (int i = 0; i < windows.length; i++) {
            if (windows[i].isDisplayable()) {
               SwingUtilities.updateComponentTreeUI(windows[i]);
            }
         }
      } else {
         Frame frames[] = Frame.getFrames();
         for (int i = 0; i < frames.length; i++) {
            if (frames[i].isDisplayable()) {
               SwingUtilities.updateComponentTreeUI(frames[i]);
            }
         }
      }
   }

   public boolean isUsingDefaultKeyShortcuts() {
      return isUsingDefaultKeyShortcuts;
   }

   public void setUsingDefaultKeyShortcuts(boolean isUsingDefaultKeyShortcuts) {
      this.isUsingDefaultKeyShortcuts = isUsingDefaultKeyShortcuts;
   }

}
