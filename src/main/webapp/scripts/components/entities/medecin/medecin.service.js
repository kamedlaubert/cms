'use strict';

angular.module('cmsApp')
    .factory('Medecin', function ($resource, DateUtils) {
        return $resource('api/medecins/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
