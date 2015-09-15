'use strict';

angular.module('cmsApp').controller('BPCDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'BPC', 'Hospitalisation',
        function($scope, $stateParams, $modalInstance, entity, BPC, Hospitalisation) {

        $scope.bPC = entity;
        $scope.hospitalisations = Hospitalisation.query();
        $scope.load = function(id) {
            BPC.get({id : id}, function(result) {
                $scope.bPC = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:bPCUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.bPC.id != null) {
                BPC.update($scope.bPC, onSaveFinished);
            } else {
                BPC.save($scope.bPC, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
