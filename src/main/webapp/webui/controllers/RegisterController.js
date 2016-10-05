angular.module('App.Auth')
    .controller('RegisterController', RegisterController);

RegisterController.$inject = ['$location', '$scope', '$rootScope', 'AuthService', 'AlertService', 'FlashMessage'];
function RegisterController($location, $scope, $rootScope, AuthService, AlertService, FlashMessage) {
    var rc = this;
    rc.register = function (admin) {
        $rootScope.isSubmitted = true;
        rc.dataLoading = true;
        rc.user.admin = admin;
        AuthService.register(rc.user, function (response) {
            if (response.code==200) {
                AuthService.createJWTToken(response.result.user, response.result.token);
                AuthService.setCredentials();
                $location.path('/app');
            } else {
                //rc.error = response.result;
                rc.details = response.details;
                rc.dataLoading = false;
                $rootScope.isSubmitted = false;
                AlertService.add('danger', response.result);
            }
        });
    };
};