var TGWebModule = angular.module('TGWeb',
                                    [
                                        'ngAnimate',
                                        'ngMessages',
                                        'ngRoute',
                                        'ngCookies',
                                        'App.Common',
                                        'App.Admin',
                                        'App.Auth',
                                        'App'
                                    ]);

TGWebModule.config(['$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {
        $routeProvider
            .when('/home', {
                controller: 'HomeController',
                templateUrl: 'webui/views/home.html',
                controllerAs: 'home'
            })
            .when('/admin.login', {
                controller: 'LoginController',
                templateUrl: 'webui/views/login.html',
                controllerAs: 'lc'
            })
            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'webui/views/login.html',
                controllerAs: 'lc'
            })
            .when('/resetpw', {
                controller: 'LoginController',
                templateUrl: 'webui/views/resetpw.html',
                controllerAs: 'lc'
            })
            .when('/register', {
                controller: 'RegisterController',
                templateUrl: 'webui/views/register.html',
                controllerAs: 'rc'
            })
            .when('/access-denied', {
                controller: 'LoginController',
                templateUrl: 'webui/views/access-denied.html',
                controllerAs: 'lc'
            })
            .when('/admin', {
                controller: 'AdminController',
                templateUrl: 'webui/views/dashboard.html',
                controllerAs: 'adm'
            })
            .when('/app', {
                controller: 'AppController',
                templateUrl: 'webui/views/dashboard.html',
                controllerAs: 'app'
            })
            .when('/changepw', {
                controller: 'AppController',
                templateUrl: 'webui/views/changepw.html',
                controllerAs: 'app'
            })
            .when('/user', {
                //controller: 'UserController',
                templateUrl: 'webui/views/user.html',
                controllerAs: 'app'
            })
            .when('/role', {
                //controller: 'RoleController',
                templateUrl: 'webui/views/role.html',
                controllerAs: 'app'
            })
            .when('/permission', {
                //controller: 'PermissionController',
                templateUrl: 'webui/views/permission.html',
                controllerAs: 'app'
            })
            .when('/organization', {
                //controller: 'OrganizationController',
                templateUrl: 'webui/views/organization.html',
                controllerAs: 'app'
            })

            .otherwise({ redirectTo: '/home' });
        
        $httpProvider.interceptors.push('responseObserver');
    }
]);

TGWebModule.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + $rootScope.globals.token;
            $rootScope.currentUser = $rootScope.globals.currentUser;
        }

        $rootScope.isSubmitted = false;

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            //console.log('received event: ' + event + ' from: ' + current + ' to go to next: ' + next);
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login','/register','/admin.login','/adm.register','/admin','/resetpw','/access-denied']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            $rootScope.currentUser = $rootScope.globals.currentUser;
            if (restrictedPage && !loggedIn) {
                if($location.path().indexOf('admin') > -1) {
                    $location.path('/admin.login');
                } else {
                    $location.path('/login');
                }
            }
        });
    }
]);

TGWebModule.factory('responseObserver', function responseObserver($q, $location) {
    return {
        'responseError': function(errorResponse) {
            switch (errorResponse.status) {
            case 401:
                $location.path('/access-denied');
                break;
            case 403:
                $location.path('/access-denied');
                break;
            case 500:
                $location.path('/access-denied');
                break;
            }
            //return $q.reject(errorResponse);
            return $q.reject();
        }
    };
});