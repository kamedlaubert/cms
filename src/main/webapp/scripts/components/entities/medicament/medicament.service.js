'use strict';

angular.module('cmsApp')
    .factory('Medicament', function ($resource, DateUtils) {
        return $resource('api/medicaments/:id', {}, {
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
