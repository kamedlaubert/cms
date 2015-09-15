'use strict';

angular.module('cmsApp')
    .factory('Personnel_Externe', function ($resource, DateUtils) {
        return $resource('api/personnel_Externes/:id', {}, {
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
