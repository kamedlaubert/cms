'use strict';

angular.module('cmsApp')
    .factory('Conjoint', function ($resource, DateUtils) {
        return $resource('api/conjoints/:id', {}, {
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
