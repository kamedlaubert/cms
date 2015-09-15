'use strict';

angular.module('cmsApp')
    .factory('Infirmirere', function ($resource, DateUtils) {
        return $resource('api/infirmireres/:id', {}, {
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
