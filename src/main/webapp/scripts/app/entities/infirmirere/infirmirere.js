'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('infirmirere', {
                parent: 'entity',
                url: '/infirmireres',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.infirmirere.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/infirmirere/infirmireres.html',
                        controller: 'InfirmirereController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('infirmirere');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('infirmirere.detail', {
                parent: 'entity',
                url: '/infirmirere/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.infirmirere.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/infirmirere/infirmirere-detail.html',
                        controller: 'InfirmirereDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('infirmirere');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Infirmirere', function($stateParams, Infirmirere) {
                        return Infirmirere.get({id : $stateParams.id});
                    }]
                }
            })
            .state('infirmirere.new', {
                parent: 'infirmirere',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/infirmirere/infirmirere-dialog.html',
                        controller: 'InfirmirereDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('infirmirere', null, { reload: true });
                    }, function() {
                        $state.go('infirmirere');
                    })
                }]
            })
            .state('infirmirere.edit', {
                parent: 'infirmirere',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/infirmirere/infirmirere-dialog.html',
                        controller: 'InfirmirereDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Infirmirere', function(Infirmirere) {
                                return Infirmirere.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('infirmirere', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
