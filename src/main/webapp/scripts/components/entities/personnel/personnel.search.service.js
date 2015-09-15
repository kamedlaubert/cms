'use strict';

angular.module('cmsApp')
    .factory('PersonnelSearch', function ($resource) {
        return $resource('api/_search/personnels/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
