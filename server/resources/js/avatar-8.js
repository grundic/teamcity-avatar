/*
 * Add avatar for current user to user panel.
 */
Avatar.addAvatarToUserPanel = function () {
  this._getAvatarUrl(Avatar.currentUser.extendedName, function (param_hash) {
    if ($j("#avatar-profile").length == 0) {
      $j("#sp_span_usernamePopup").before('<img id="avatar-profile" class="avatar" src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
    } else {
      $j("#avatar-profile").attr("src", param_hash['avatarUrl']);
    }
  });
};


$j(document).ready(function () {
  Avatar.addAvatarToUserPanel();
  Avatar.showPopupNearElementHack();
  Avatar.addAvatarToPendingChangesDivTab();
});