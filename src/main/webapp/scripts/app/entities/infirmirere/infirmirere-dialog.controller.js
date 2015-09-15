'use strict';

angular.module('cmsApp').controller('InfirmirereDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Infirmirere',
        function($scope, $stateParams, $modalInstance, entity, Infirmirere) {

        $scope.infirmirere = entity;
        $scope.load = function(id) {
            Infirmirere.get({id : id}, function(result) {
                $scope.infirmirere = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:infirmirereUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.infirmirere.id != null) {
                Infirmirere.update($scope.infirmirere, onSaveFinished);
            } else {
                Infirmirere.save($scope.infirmirere, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
