angular.module('App.Admin')
    .controller('AdminController', ['$rootScope', '$location', AdminController]);

function AdminController($rootScope, $location) {
    var admin = this;
    admin.currentUser = null;
};