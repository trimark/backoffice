angular.module('App.Auth')
    .controller('LoginController', ['$scope', '$rootScope', '$location', 'AuthService', 'AlertService', LoginController]);

function LoginController($scope, $rootScope, $location, AuthService, AlertService) {
    var lc = this;

    (function initController() {
        // reset login status
        lc.dataLoading = false;
        $rootScope.isSubmitted = false;
        AuthService.clearCredentials();
    })();

    lc.login = function () {
        lc.dataLoading = true;
        $rootScope.isSubmitted = true;
        AuthService.login(lc.user.email, lc.user.password, function (response) {
            lc.dataLoading = false;
            $rootScope.isSubmitted = false;
            if (response.code==200) {
                AuthService.createJWTToken(response.result.user, response.result.token);
                AuthService.setCredentials();
                $location.path('/app');
            } else {
                //lc.error = response.result;
                lc.details = response.details;
                AlertService.add('danger', response.result);
            }
        });
    };

    lc.resetpw = function () {
        lc.dataLoading = true;
        $rootScope.isSubmitted = true;
        AuthService.resetPassword($scope.resetPasswordEmail, function (response) {
            lc.dataLoading = false;
            $rootScope.isSubmitted = false;
            if (response.status==200) {
                $location.path('/login');
                AlertService.add('success', 'Password reset successfully.');
            } else {
                //lc.error = response.result;
                lc.details = response.details;
                AlertService.add('danger', 'Password reset failed.');
            }
        });
    };

};