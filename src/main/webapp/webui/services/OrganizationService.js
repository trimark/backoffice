'use strict';
angular.module('App').
	factory('OrganizationService',['$http', '$q', 'BackendCfg', function($http, $q, BackendCfg){

	return {
		
			fetchAllOrganizations: function() {
					return $http.get(BackendCfg.url+'/api/organization/list/')
							.then(
									function(response){
										return response.data;
									}, 
									function(errResponse){
										return $q.reject(errResponse);
									}
							);
			},
		    
		    createOrganization: function(organization){
					return $http.post(BackendCfg.url+'/api/organization/add/', organization)
							.then(
									function(response){
										return response.data;
									}, 
									function(errResponse){
										return $q.reject(errResponse);
									}
							);
		    },
		    
		    updateOrganization: function(organization, id){
					return $http.put(BackendCfg.url+'/api/organization/edit/'+id, organization)
							.then(
									function(response){
										return response.data;
									}, 
									function(errResponse){
										return $q.reject(errResponse);
									}
							);
			},
		    
			deleteOrganization: function(id){
					return $http.delete(BackendCfg.url+'/api/organization/delete/'+id)
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