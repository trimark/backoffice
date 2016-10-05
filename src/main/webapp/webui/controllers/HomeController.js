angular.module('App')
    .controller('HomeController', HomeController);

function HomeController($scope, $location) {
    var home = this;
    home.currentUser = null;
};