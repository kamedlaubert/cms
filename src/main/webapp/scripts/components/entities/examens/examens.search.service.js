'use strict';

angular.module('cmsApp')
    .factory('ExamensSearch', function ($resource) {
        return $resource('api/_search/examenss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
