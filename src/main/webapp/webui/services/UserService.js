'use strict';
angular.module('App').
	factory('UserService',['$http', '$q', 'BackendCfg', function($http, $q, BackendCfg){

	return {
		
		fetchAllUsers: function() {
				return $http.get(BackendCfg.url+'/api/user/list/')
						.then(
								function(response){
									return response.data;
								}, 
								function(errResponse){
									return $q.reject(errResponse);
								}
						);
		},
	    
	    createUser: function(user){
				return $http.post(BackendCfg.url+'/api/user/add/', user)
						.then(
								function(response){
									return response.data;
								}, 
								function(errResponse){
									return $q.reject(errResponse);
								}
						);
	    },
	    
	    updateUser: function(user, id){
				return $http.put(BackendCfg.url+'/api/user/edit/'+id, user)
						.then(
								function(response){
									return response.data;
								}, 
								function(errResponse){
									return $q.reject(errResponse);
								}
						);
		},
	    
		deleteUser: function(id){
				return $http.delete(BackendCfg.url+'/api/user/delete/'+id)
						.then(
								function(response){
									return response.data;
								}, 
								function(errResponse){
									return $q.reject(errResponse);
								}
						);
	    }
	};

}]);