package io.th.hologramview;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.EventDispatcher;
import android.view.ViewGroup;

@com.google.appinventor.components.annotations.Extension(
        description = "A futuristic native hologram profile card component. highly animated \"Holographic Profile Card\" into your Android apps. This extension uses purely native Android views (no WebViewer!) to recreate advanced CSS animations, including floating skewed projections and blinking projector light effects. Developed by TechHamara Ussing Fast. <br><a href='https://github.com/TechHamara/Th_Free_Extensions' target='_blank'><small><u>Find More Extension</u></small></a><br><a href='https://github.com/TechHamara/Th_Extensions_List/blob/main/LICENSE.md#terms-and-conditions-for-the-extension' target='_blank'><small><u>Terms & Conditions</u></small></a><br><a href='https://buymeacoffee.com/techhamara' target='_blank'><small><u>Find More On BuyMeCoffee Page</u></small></a>",
        version = "2",
        versionName = "1.0",
        icon = "icon.png"
)
public class HologramView extends AndroidNonvisibleComponent implements HologramLayout.HologramListener {

  private ComponentContainer container;
  private HologramLayout hologramLayout;

  private String profileName = "TechHamara";
  private String profileTitle = "Developer";
  private String profileStats = "Group 216 | Code 90 | Grade 40";
  private String buttonText = "FOLLOW ME";
  private String imageUrl = "https://ibb.co/tTtSLy4Z";
  private int hologramStartColor = 0xFF1E50B4;
  private int hologramEndColor = 0xFF5A8CF0;
  private int projectorColor = 0xFF505057;
  private int buttonStartColor = 0xFF3C6ED2;
  private int buttonEndColor = 0xFF6496FA;
  private int textColor = 0xFFFFFFFF;
  private int statsColor = 0xFFC8C8CD;
  private int hologramSpeed = 15000;
  private int homePageGradientStartColor = 0xFF0A0A11;
  private int homePageGradientEndColor = 0xFF323239;
  private int homePageColor = 0;
  private String homePageImage = "";
  private String hologramImage = "";

  public HologramView(ComponentContainer container) {
    super(container.$form());
    this.container = container;
  }

  @SimpleFunction(description = "Initialize the hologram inside a given arrangement (Horizontal, Vertical, etc).")
  public void Initialize(AndroidViewComponent arrangement) {
    if (hologramLayout == null) {
      CompanionHelper helper = new CompanionHelper(container);
      hologramLayout = new HologramLayout(container.$context(), this, helper);
      hologramLayout.setProfileName(profileName);
      hologramLayout.setTitle(profileTitle);
      hologramLayout.setStats(profileStats);
      hologramLayout.setButtonText(buttonText);
      hologramLayout.loadProfileImage(imageUrl);
      hologramLayout.setHologramColors(hologramStartColor, hologramEndColor);
      hologramLayout.setProjectorColor(projectorColor);
      hologramLayout.setButtonColors(buttonStartColor, buttonEndColor);
      hologramLayout.setTextColor(textColor, textColor);
      hologramLayout.setStatsColor(statsColor);
      hologramLayout.setHologramSpeed(hologramSpeed);
      hologramLayout.setBackgroundGradient(homePageGradientStartColor, homePageGradientEndColor);
      if (homePageColor != 0)
        hologramLayout.setBackgroundColor(homePageColor);
      if (homePageImage != null && !homePageImage.isEmpty())
        hologramLayout.setBackgroundImage(homePageImage);
      if (hologramImage != null && !hologramImage.isEmpty())
        hologramLayout.setHologramBackgroundImage(hologramImage);

      ViewGroup viewGroup = (ViewGroup) arrangement.getView();
      viewGroup.addView(hologramLayout);
    }
  }

  @SimpleFunction(description = "Open the hologram card animation.")
  public void Open() {
    if (hologramLayout != null)
      hologramLayout.open();
  }

  @SimpleFunction(description = "Close the hologram card animation.")
  public void Close() {
    if (hologramLayout != null)
      hologramLayout.close();
  }

  @SimpleFunction(description = "Toggle the hologram card between open and closed.")
  public void Toggle() {
    if (hologramLayout != null)
      hologramLayout.toggle();
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "TechHamara")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the profile name.")
  public void ProfileName(String name) {
    this.profileName = name;
    if (hologramLayout != null)
      hologramLayout.setProfileName(name);
  }

  @SimpleProperty(description = "Get the profile name.")
  public String ProfileName() {
    return profileName;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "Developer")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the profile title.")
  public void ProfileTitle(String title) {
    this.profileTitle = title;
    if (hologramLayout != null)
      hologramLayout.setTitle(title);
  }

  @SimpleProperty(description = "Get the profile title.")
  public String ProfileTitle() {
    return profileTitle;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "Group 216 | Code 90 | Grade 40")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the profile stats text.")
  public void ProfileStats(String stats) {
    this.profileStats = stats;
    if (hologramLayout != null)
      hologramLayout.setStats(stats);
  }

  @SimpleProperty(description = "Get the profile stats text.")
  public String ProfileStats() {
    return profileStats;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "FOLLOW ME")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the button text.")
  public void ButtonText(String text) {
    this.buttonText = text;
    if (hologramLayout != null)
      hologramLayout.setButtonText(text);
  }

  @SimpleProperty(description = "Get the button text.")
  public String ButtonText() {
    return buttonText;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "https://ibb.co/tTtSLy4Z")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the profile image URL.")
  public void ProfileImageUrl(String url) {
    this.imageUrl = url;
    if (hologramLayout != null)
      hologramLayout.loadProfileImage(url);
  }

  @SimpleProperty(description = "Get the profile image URL.")
  public String ProfileImageUrl() {
    return imageUrl;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFF1E50B4")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the hologram start color.")
  public void HologramStartColor(int color) {
    this.hologramStartColor = color;
    if (hologramLayout != null)
      hologramLayout.setHologramColors(color, hologramEndColor);
  }

  @SimpleProperty(description = "Get the hologram start color.")
  public int HologramStartColor() {
    return hologramStartColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFF5A8CF0")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the hologram end color.")
  public void HologramEndColor(int color) {
    this.hologramEndColor = color;
    if (hologramLayout != null)
      hologramLayout.setHologramColors(hologramStartColor, color);
  }

  @SimpleProperty(description = "Get the hologram end color.")
  public int HologramEndColor() {
    return hologramEndColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFF505057")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the projector color.")
  public void ProjectorColor(int color) {
    this.projectorColor = color;
    if (hologramLayout != null)
      hologramLayout.setProjectorColor(color);
  }

  @SimpleProperty(description = "Get the projector color.")
  public int ProjectorColor() {
    return projectorColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFF3C6ED2")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the button start color.")
  public void ButtonStartColor(int color) {
    this.buttonStartColor = color;
    if (hologramLayout != null)
      hologramLayout.setButtonColors(color, buttonEndColor);
  }

  @SimpleProperty(description = "Get the button start color.")
  public int ButtonStartColor() {
    return buttonStartColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFF6496FA")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the button end color.")
  public void ButtonEndColor(int color) {
    this.buttonEndColor = color;
    if (hologramLayout != null)
      hologramLayout.setButtonColors(buttonStartColor, color);
  }

  @SimpleProperty(description = "Get the button end color.")
  public int ButtonEndColor() {
    return buttonEndColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFFFFFFFF")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the text color.")
  public void TextColor(int color) {
    this.textColor = color;
    if (hologramLayout != null)
      hologramLayout.setTextColor(color, color);
  }

  @SimpleProperty(description = "Get the text color.")
  public int TextColor() {
    return textColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFFC8C8CD")
  @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Set the stats color.")
  public void StatsColor(int color) {
    this.statsColor = color;
    if (hologramLayout != null)
      hologramLayout.setStatsColor(color);
  }

  @SimpleProperty(description = "Get the stats color.")
  public int StatsColor() {
    return statsColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "15000")
  @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set the hologram speed.")
  public void HologramSpeed(int durationMs) {
    this.hologramSpeed = durationMs;
    if (hologramLayout != null)
      hologramLayout.setHologramSpeed(durationMs);
  }

  @SimpleProperty(description = "Get the hologram speed.")
  public int HologramSpeed() {
    return hologramSpeed;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFF0A0A11")
  @SimpleProperty(category = PropertyCategory.APPEARANCE)
  public void BgGradientStartColor(int color) {
    this.homePageGradientStartColor = color;
    if (hologramLayout != null)
      hologramLayout.setBackgroundGradient(color, homePageGradientEndColor);
  }

  @SimpleProperty(description = "Get the home page gradient start color.")
  public int BgGradientStartColor() {
    return homePageGradientStartColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&HFF323239")
  @SimpleProperty(category = PropertyCategory.APPEARANCE)
  public void BgGradientEndColor(int color) {
    this.homePageGradientEndColor = color;
    if (hologramLayout != null)
      hologramLayout.setBackgroundGradient(homePageGradientStartColor, color);
  }

  @SimpleProperty(description = "Get the home page gradient end color.")
  public int BgGradientEndColor() {
    return homePageGradientEndColor;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = "&H00000000")
  @SimpleProperty(category = PropertyCategory.APPEARANCE)
  public void BgColor(int color) {
    this.homePageColor = color;
    if (hologramLayout != null && color != 0)
      hologramLayout.setBackgroundColor(color);
  }

  @SimpleProperty(description = "Get the home page color.")
  public int BgColor() {
    return homePageColor;
  }

  @SimpleProperty(description = "Set home page image of url or assets")
  public void BgImage(String url) {
    this.homePageImage = url;
    if (hologramLayout != null)
      hologramLayout.setBackgroundImage(url);
  }

  @SimpleProperty(description = "Get the home page image.")
  public String BgImage() {
    return homePageImage;
  }

  @SimpleProperty(description = "Set the background image of the hologram projection from URL or asset.")
  public void HologramImage(String url) {
    this.hologramImage = url;
    if (hologramLayout != null)
      hologramLayout.setHologramBackgroundImage(url);
  }

  @SimpleProperty(description = "Get the hologram image.")
  public String HologramImage() {
    return hologramImage;
  }

  // Events
  @Override
  public void onOpened() {
    Opened();
  }

  @Override
  public void onClosed() {
    Closed();
  }

  @Override
  public void onFollowClicked() {
    ButtonClicked();
  }

  @Override
  public void onProfileImageClicked() {
    ProfileImageClicked();
  }

  @Override
  public void onProfileNameClicked() {
    ProfileNameClicked();
  }

  @Override
  public void onProfileTitleClicked() {
    ProfileTitleClicked();
  }

  @Override
  public void onProfileStatsClicked() {
    ProfileStatsClicked();
  }

  @SimpleEvent(description = "Triggered when the hologram opens fully.")
  public void Opened() {
    EventDispatcher.dispatchEvent(this, "Opened");
  }

  @SimpleEvent(description = "Triggered when the hologram closes fully.")
  public void Closed() {
    EventDispatcher.dispatchEvent(this, "Closed");
  }

  @SimpleEvent(description = "Triggered when the follow button is clicked.")
  public void ButtonClicked() {
    EventDispatcher.dispatchEvent(this, "ButtonClicked");
  }

  @SimpleEvent(description = "Triggered when the profile image is tapped.")
  public void ProfileImageClicked() {
    EventDispatcher.dispatchEvent(this, "ProfileImageClicked");
  }

  @SimpleEvent(description = "Triggered when the profile name is tapped.")
  public void ProfileNameClicked() {
    EventDispatcher.dispatchEvent(this, "ProfileNameClicked");
  }

  @SimpleEvent(description = "Triggered when the profile title is tapped.")
  public void ProfileTitleClicked() {
    EventDispatcher.dispatchEvent(this, "ProfileTitleClicked");
  }

  @SimpleEvent(description = "Triggered when the profile stats are tapped.")
  public void ProfileStatsClicked() {
    EventDispatcher.dispatchEvent(this, "ProfileStatsClicked");
  }
}
