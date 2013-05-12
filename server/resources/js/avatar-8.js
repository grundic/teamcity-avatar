/*
 * Add avatar for current user to user panel.
 */
Avatar.addAvatarToUserPanel = function () {
  this._getAvatarUrl(Avatar.currentUser.extendedName, function (param_hash) {
    $j("#sp_span_usernamePopup").before('<img class="avatar" src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
  });
};


$j(document).ready(function () {
  Avatar.addAvatarToUserPanel();
  Avatar.showPopupNearElementHack();
  Avatar.addAvatarToPendingChangesDivTab();
});