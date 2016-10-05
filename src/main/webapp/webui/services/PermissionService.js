'use strict';
angular.module('App').
	factory('PermissionService',['$http', '$q', 'BackendCfg', function($http, $q, BackendCfg){

	return {
		
			fetchAllPermissions: function() {
					return $http.get(BackendCfg.url+'/api/permission/list/')
							.then(
									function(response){
										return response.data;
									}, 
									function(errResponse){
										return $q.reject(errResponse);
									}
							);
			},
		    
		    createPermission: function(permission){
					return $http.post(BackendCfg.url+'/api/permission/add/', permission)
							.then(
									function(response){
										return response.data;
									}, 
									function(errResponse){
										return $q.reject(errResponse);
									}
							);
		    },
		    
		    updatePermission: function(permission, id){
					return $http.put(BackendCfg.url+'/api/permission/edit/'+id, permission)
							.then(
									function(response){
										return response.data;
									}, 
									function(errResponse){
										return $q.reject(errResponse);
									}
							);
			},
		    
			deletePermission: function(id){
					return $http.delete(BackendCfg.url+'/api/permission/delete/'+id)
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