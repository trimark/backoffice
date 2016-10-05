angular.module('App')
    .controller('AppController', AppController);

AppController.$inject = ['$location', '$scope', '$rootScope', 'AuthService', 'AlertService', 'FlashMessage'];
function AppController($location, $scope, $rootScope, AuthService, AlertService, FlashMessage) {
    var app = this;

    app.logout = function () {
        AuthService.logout(function (response) {
            AuthService.clearCredentials();
            $location.path('/');
        });
    };

    app.changepw = function () {
        AuthService.changePassword($scope.newpassword, function (response) {
            if (response.status==200) {
                AuthService.clearCredentials();
                $location.path('/login');
                AlertService.add('success', 'Password changed successfully.');
            } else {
                AlertService.add('danger', 'Password change failed.');
            }
        });
    };
};
