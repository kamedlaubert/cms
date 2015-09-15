'use strict';

angular.module('cmsApp')
    .factory('Patient', function ($resource, DateUtils) {
        return $resource('api/patients/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateNaissance = DateUtils.convertLocaleDateFromServer(data.dateNaissance);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateNaissance = DateUtils.convertLocaleDateToServer(data.dateNaissance);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateNaissance = DateUtils.convertLocaleDateToServer(data.dateNaissance);
                    return angular.toJson(data);
                }
            }
        });
    });
