(function () {

    'use strict';

    angular.module('App').controller('BodyController', BodyController);

    BodyController.$inject = ['$scope', 'AlertService'];

    function BodyController($scope, AlertService) {
        $scope.alerts = AlertService.get();
    }
})();