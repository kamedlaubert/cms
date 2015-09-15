'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('conjoint', {
                parent: 'entity',
                url: '/conjoints',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.conjoint.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/conjoint/conjoints.html',
                        controller: 'ConjointController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('conjoint');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('conjoint.detail', {
                parent: 'entity',
                url: '/conjoint/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.conjoint.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/conjoint/conjoint-detail.html',
                        controller: 'ConjointDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('conjoint');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Conjoint', function($stateParams, Conjoint) {
                        return Conjoint.get({id : $stateParams.id});
                    }]
                }
            })
            .state('conjoint.new', {
                parent: 'conjoint',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/conjoint/conjoint-dialog.html',
                        controller: 'ConjointDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('conjoint', null, { reload: true });
                    }, function() {
                        $state.go('conjoint');
                    })
                }]
            })
            .state('conjoint.edit', {
                parent: 'conjoint',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/conjoint/conjoint-dialog.html',
                        controller: 'ConjointDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Conjoint', function(Conjoint) {
                                return Conjoint.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('conjoint', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
