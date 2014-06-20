/*
 * Add avatar for current user to user panel.
 */
Avatar.addAvatarToUserPanel = function () {
  $this = this;
  this._getAvatarUrl({"username":Avatar.currentUser.extendedName}, function (param_hash) {
    if ($j("#avatar-profile").length == 0) {
      $j("#sp_span_usernamePopup").before('<img id="avatar-profile" class="avatar" src="' + param_hash['avatarUrl'] + '">');
      $this.addBigAvatar(Avatar.currentUser.extendedName, param_hash['avatarUrl'], $j("#avatar-profile"));
    } else {
      $j("#avatar-profile").attr("src", param_hash['avatarUrl']);
    }
  });
};


$j(document).ready(function () {
  Avatar.addAvatarToUserPanel();
  Avatar.showPopupNearElementHack();
  Avatar.addAvatarToPendingChangesDivTab();
  Avatar.addAvatarToTriggeredByBuild();
  Avatar.addAvatarToTriggeredByQueuedBuild();
});