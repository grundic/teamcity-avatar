/*
 * Add avatar for current user to user panel.
 */
Avatar.addAvatarToUserPanel = function () {
  this._getAvatarUrl(null, function (param_hash) {
    $j("#sp_span_usernamePopup").before('<img src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
  });
};


$j(document).ready(function () {
  Avatar.addAvatarToUserPanel();
  Avatar.showPopupNearElementHack();
  Avatar.addAvatarToPendingChangesDivTab();
});