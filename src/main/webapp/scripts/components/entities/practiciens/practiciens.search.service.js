'use strict';

angular.module('cmsApp')
    .factory('PracticiensSearch', function ($resource) {
        return $resource('api/_search/practicienss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
