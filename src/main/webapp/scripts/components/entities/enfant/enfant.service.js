'use strict';

angular.module('cmsApp')
    .factory('Enfant', function ($resource, DateUtils) {
        return $resource('api/enfants/:id', {}, {
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
