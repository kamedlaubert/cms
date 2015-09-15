'use strict';

angular.module('cmsApp')
    .factory('PatientSearch', function ($resource) {
        return $resource('api/_search/patients/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
